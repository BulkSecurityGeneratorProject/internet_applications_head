package head.web.rest;

import head.HeadApp;

import head.domain.Magazine;
import head.repository.MagazineRepository;
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

/**
 * Test class for the MagazineResource REST controller.
 *
 * @see MagazineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HeadApp.class)
public class MagazineResourceIntTest {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMagazineMockMvc;

    private Magazine magazine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MagazineResource magazineResource = new MagazineResource(magazineRepository);
        this.restMagazineMockMvc = MockMvcBuilders.standaloneSetup(magazineResource)
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
    public static Magazine createEntity(EntityManager em) {
        Magazine magazine = new Magazine()
            .location(DEFAULT_LOCATION)
            .capacity(DEFAULT_CAPACITY);
        return magazine;
    }

    @Before
    public void initTest() {
        magazine = createEntity(em);
    }

    @Test
    @Transactional
    public void createMagazine() throws Exception {
        int databaseSizeBeforeCreate = magazineRepository.findAll().size();

        // Create the Magazine
        restMagazineMockMvc.perform(post("/api/magazines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magazine)))
            .andExpect(status().isCreated());

        // Validate the Magazine in the database
        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeCreate + 1);
        Magazine testMagazine = magazineList.get(magazineList.size() - 1);
        assertThat(testMagazine.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testMagazine.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    public void createMagazineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = magazineRepository.findAll().size();

        // Create the Magazine with an existing ID
        magazine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagazineMockMvc.perform(post("/api/magazines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magazine)))
            .andExpect(status().isBadRequest());

        // Validate the Magazine in the database
        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazineRepository.findAll().size();
        // set the field null
        magazine.setLocation(null);

        // Create the Magazine, which fails.

        restMagazineMockMvc.perform(post("/api/magazines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magazine)))
            .andExpect(status().isBadRequest());

        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = magazineRepository.findAll().size();
        // set the field null
        magazine.setCapacity(null);

        // Create the Magazine, which fails.

        restMagazineMockMvc.perform(post("/api/magazines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magazine)))
            .andExpect(status().isBadRequest());

        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMagazines() throws Exception {
        // Initialize the database
        magazineRepository.saveAndFlush(magazine);

        // Get all the magazineList
        restMagazineMockMvc.perform(get("/api/magazines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magazine.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    @Test
    @Transactional
    public void getMagazine() throws Exception {
        // Initialize the database
        magazineRepository.saveAndFlush(magazine);

        // Get the magazine
        restMagazineMockMvc.perform(get("/api/magazines/{id}", magazine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(magazine.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test
    @Transactional
    public void getNonExistingMagazine() throws Exception {
        // Get the magazine
        restMagazineMockMvc.perform(get("/api/magazines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMagazine() throws Exception {
        // Initialize the database
        magazineRepository.saveAndFlush(magazine);
        int databaseSizeBeforeUpdate = magazineRepository.findAll().size();

        // Update the magazine
        Magazine updatedMagazine = magazineRepository.findOne(magazine.getId());
        // Disconnect from session so that the updates on updatedMagazine are not directly saved in db
        em.detach(updatedMagazine);
        updatedMagazine
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY);

        restMagazineMockMvc.perform(put("/api/magazines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMagazine)))
            .andExpect(status().isOk());

        // Validate the Magazine in the database
        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeUpdate);
        Magazine testMagazine = magazineList.get(magazineList.size() - 1);
        assertThat(testMagazine.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMagazine.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void updateNonExistingMagazine() throws Exception {
        int databaseSizeBeforeUpdate = magazineRepository.findAll().size();

        // Create the Magazine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMagazineMockMvc.perform(put("/api/magazines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magazine)))
            .andExpect(status().isCreated());

        // Validate the Magazine in the database
        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMagazine() throws Exception {
        // Initialize the database
        magazineRepository.saveAndFlush(magazine);
        int databaseSizeBeforeDelete = magazineRepository.findAll().size();

        // Get the magazine
        restMagazineMockMvc.perform(delete("/api/magazines/{id}", magazine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Magazine> magazineList = magazineRepository.findAll();
        assertThat(magazineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Magazine.class);
        Magazine magazine1 = new Magazine();
        magazine1.setId(1L);
        Magazine magazine2 = new Magazine();
        magazine2.setId(magazine1.getId());
        assertThat(magazine1).isEqualTo(magazine2);
        magazine2.setId(2L);
        assertThat(magazine1).isNotEqualTo(magazine2);
        magazine1.setId(null);
        assertThat(magazine1).isNotEqualTo(magazine2);
    }
}
