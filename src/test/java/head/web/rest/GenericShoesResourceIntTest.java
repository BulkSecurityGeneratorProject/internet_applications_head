package head.web.rest;

import head.HeadApp;

import head.domain.GenericShoes;
import head.repository.GenericShoesRepository;
import head.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static head.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import head.domain.enumeration.SHOETYPES;
import head.domain.enumeration.SHOEBRANDS;
import head.domain.enumeration.SHOECOLORS;
/**
 * Test class for the GenericShoesResource REST controller.
 *
 * @see GenericShoesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HeadApp.class)
public class GenericShoesResourceIntTest {

    private static final SHOETYPES DEFAULT_SHOE_TYPE = SHOETYPES.WOMAN;
    private static final SHOETYPES UPDATED_SHOE_TYPE = SHOETYPES.MAN;

    private static final SHOEBRANDS DEFAULT_BRAND = SHOEBRANDS.NIKE;
    private static final SHOEBRANDS UPDATED_BRAND = SHOEBRANDS.ADIDAS;

    private static final SHOECOLORS DEFAULT_COLOR = SHOECOLORS.BLACK;
    private static final SHOECOLORS UPDATED_COLOR = SHOECOLORS.WHITE;

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;

    @Autowired
    private GenericShoesRepository genericShoesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGenericShoesMockMvc;

    private GenericShoes genericShoes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GenericShoesResource genericShoesResource = new GenericShoesResource(genericShoesRepository);
        this.restGenericShoesMockMvc = MockMvcBuilders.standaloneSetup(genericShoesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenericShoes createEntity(EntityManager em) {
        GenericShoes genericShoes = new GenericShoes()
            .shoeType(DEFAULT_SHOE_TYPE)
            .brand(DEFAULT_BRAND)
            .color(DEFAULT_COLOR)
            .size(DEFAULT_SIZE);
        return genericShoes;
    }

    @Before
    public void initTest() {
        genericShoes = createEntity(em);
    }

    @Test
    @Transactional
    public void createGenericShoes() throws Exception {
        int databaseSizeBeforeCreate = genericShoesRepository.findAll().size();

        // Create the GenericShoes
        restGenericShoesMockMvc.perform(post("/api/generic-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genericShoes)))
            .andExpect(status().isCreated());

        // Validate the GenericShoes in the database
        List<GenericShoes> genericShoesList = genericShoesRepository.findAll();
        assertThat(genericShoesList).hasSize(databaseSizeBeforeCreate + 1);
        GenericShoes testGenericShoes = genericShoesList.get(genericShoesList.size() - 1);
        assertThat(testGenericShoes.getShoeType()).isEqualTo(DEFAULT_SHOE_TYPE);
        assertThat(testGenericShoes.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testGenericShoes.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testGenericShoes.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    public void createGenericShoesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = genericShoesRepository.findAll().size();

        // Create the GenericShoes with an existing ID
        genericShoes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenericShoesMockMvc.perform(post("/api/generic-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genericShoes)))
            .andExpect(status().isBadRequest());

        // Validate the GenericShoes in the database
        List<GenericShoes> genericShoesList = genericShoesRepository.findAll();
        assertThat(genericShoesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = genericShoesRepository.findAll().size();
        // set the field null
        genericShoes.setSize(null);

        // Create the GenericShoes, which fails.

        restGenericShoesMockMvc.perform(post("/api/generic-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genericShoes)))
            .andExpect(status().isBadRequest());

        List<GenericShoes> genericShoesList = genericShoesRepository.findAll();
        assertThat(genericShoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGenericShoes() throws Exception {
        // Initialize the database
        genericShoesRepository.saveAndFlush(genericShoes);

        // Get all the genericShoesList
        restGenericShoesMockMvc.perform(get("/api/generic-shoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genericShoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].shoeType").value(hasItem(DEFAULT_SHOE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)));
    }

    @Test
    @Transactional
    public void getGenericShoes() throws Exception {
        // Initialize the database
        genericShoesRepository.saveAndFlush(genericShoes);

        // Get the genericShoes
        restGenericShoesMockMvc.perform(get("/api/generic-shoes/{id}", genericShoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(genericShoes.getId().intValue()))
            .andExpect(jsonPath("$.shoeType").value(DEFAULT_SHOE_TYPE.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE));
    }

    @Test
    @Transactional
    public void getNonExistingGenericShoes() throws Exception {
        // Get the genericShoes
        restGenericShoesMockMvc.perform(get("/api/generic-shoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenericShoes() throws Exception {
        // Initialize the database
        genericShoesRepository.saveAndFlush(genericShoes);
        int databaseSizeBeforeUpdate = genericShoesRepository.findAll().size();

        // Update the genericShoes
        GenericShoes updatedGenericShoes = genericShoesRepository.findOne(genericShoes.getId());
        // Disconnect from session so that the updates on updatedGenericShoes are not directly saved in db
        em.detach(updatedGenericShoes);
        updatedGenericShoes
            .shoeType(UPDATED_SHOE_TYPE)
            .brand(UPDATED_BRAND)
            .color(UPDATED_COLOR)
            .size(UPDATED_SIZE);

        restGenericShoesMockMvc.perform(put("/api/generic-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGenericShoes)))
            .andExpect(status().isOk());

        // Validate the GenericShoes in the database
        List<GenericShoes> genericShoesList = genericShoesRepository.findAll();
        assertThat(genericShoesList).hasSize(databaseSizeBeforeUpdate);
        GenericShoes testGenericShoes = genericShoesList.get(genericShoesList.size() - 1);
        assertThat(testGenericShoes.getShoeType()).isEqualTo(UPDATED_SHOE_TYPE);
        assertThat(testGenericShoes.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testGenericShoes.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testGenericShoes.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingGenericShoes() throws Exception {
        int databaseSizeBeforeUpdate = genericShoesRepository.findAll().size();

        // Create the GenericShoes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGenericShoesMockMvc.perform(put("/api/generic-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genericShoes)))
            .andExpect(status().isCreated());

        // Validate the GenericShoes in the database
        List<GenericShoes> genericShoesList = genericShoesRepository.findAll();
        assertThat(genericShoesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGenericShoes() throws Exception {
        // Initialize the database
        genericShoesRepository.saveAndFlush(genericShoes);
        int databaseSizeBeforeDelete = genericShoesRepository.findAll().size();

        // Get the genericShoes
        restGenericShoesMockMvc.perform(delete("/api/generic-shoes/{id}", genericShoes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GenericShoes> genericShoesList = genericShoesRepository.findAll();
        assertThat(genericShoesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenericShoes.class);
        GenericShoes genericShoes1 = new GenericShoes();
        genericShoes1.setId(1L);
        GenericShoes genericShoes2 = new GenericShoes();
        genericShoes2.setId(genericShoes1.getId());
        assertThat(genericShoes1).isEqualTo(genericShoes2);
        genericShoes2.setId(2L);
        assertThat(genericShoes1).isNotEqualTo(genericShoes2);
        genericShoes1.setId(null);
        assertThat(genericShoes1).isNotEqualTo(genericShoes2);
    }
}
