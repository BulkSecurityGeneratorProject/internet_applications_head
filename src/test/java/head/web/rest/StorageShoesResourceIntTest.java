package head.web.rest;

import head.HeadApp;

import head.domain.StorageShoes;
import head.repository.StorageShoesRepository;
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
 * Test class for the StorageShoesResource REST controller.
 *
 * @see StorageShoesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HeadApp.class)
public class StorageShoesResourceIntTest {

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    @Autowired
    private StorageShoesRepository storageShoesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStorageShoesMockMvc;

    private StorageShoes storageShoes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StorageShoesResource storageShoesResource = new StorageShoesResource(storageShoesRepository);
        this.restStorageShoesMockMvc = MockMvcBuilders.standaloneSetup(storageShoesResource)
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
    public static StorageShoes createEntity(EntityManager em) {
        StorageShoes storageShoes = new StorageShoes()
            .amount(DEFAULT_AMOUNT);
        return storageShoes;
    }

    @Before
    public void initTest() {
        storageShoes = createEntity(em);
    }

    @Test
    @Transactional
    public void createStorageShoes() throws Exception {
        int databaseSizeBeforeCreate = storageShoesRepository.findAll().size();

        // Create the StorageShoes
        restStorageShoesMockMvc.perform(post("/api/storage-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageShoes)))
            .andExpect(status().isCreated());

        // Validate the StorageShoes in the database
        List<StorageShoes> storageShoesList = storageShoesRepository.findAll();
        assertThat(storageShoesList).hasSize(databaseSizeBeforeCreate + 1);
        StorageShoes testStorageShoes = storageShoesList.get(storageShoesList.size() - 1);
        assertThat(testStorageShoes.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createStorageShoesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storageShoesRepository.findAll().size();

        // Create the StorageShoes with an existing ID
        storageShoes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageShoesMockMvc.perform(post("/api/storage-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageShoes)))
            .andExpect(status().isBadRequest());

        // Validate the StorageShoes in the database
        List<StorageShoes> storageShoesList = storageShoesRepository.findAll();
        assertThat(storageShoesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageShoesRepository.findAll().size();
        // set the field null
        storageShoes.setAmount(null);

        // Create the StorageShoes, which fails.

        restStorageShoesMockMvc.perform(post("/api/storage-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageShoes)))
            .andExpect(status().isBadRequest());

        List<StorageShoes> storageShoesList = storageShoesRepository.findAll();
        assertThat(storageShoesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStorageShoes() throws Exception {
        // Initialize the database
        storageShoesRepository.saveAndFlush(storageShoes);

        // Get all the storageShoesList
        restStorageShoesMockMvc.perform(get("/api/storage-shoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageShoes.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    public void getStorageShoes() throws Exception {
        // Initialize the database
        storageShoesRepository.saveAndFlush(storageShoes);

        // Get the storageShoes
        restStorageShoesMockMvc.perform(get("/api/storage-shoes/{id}", storageShoes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storageShoes.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingStorageShoes() throws Exception {
        // Get the storageShoes
        restStorageShoesMockMvc.perform(get("/api/storage-shoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStorageShoes() throws Exception {
        // Initialize the database
        storageShoesRepository.saveAndFlush(storageShoes);
        int databaseSizeBeforeUpdate = storageShoesRepository.findAll().size();

        // Update the storageShoes
        StorageShoes updatedStorageShoes = storageShoesRepository.findOne(storageShoes.getId());
        // Disconnect from session so that the updates on updatedStorageShoes are not directly saved in db
        em.detach(updatedStorageShoes);
        updatedStorageShoes
            .amount(UPDATED_AMOUNT);

        restStorageShoesMockMvc.perform(put("/api/storage-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStorageShoes)))
            .andExpect(status().isOk());

        // Validate the StorageShoes in the database
        List<StorageShoes> storageShoesList = storageShoesRepository.findAll();
        assertThat(storageShoesList).hasSize(databaseSizeBeforeUpdate);
        StorageShoes testStorageShoes = storageShoesList.get(storageShoesList.size() - 1);
        assertThat(testStorageShoes.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingStorageShoes() throws Exception {
        int databaseSizeBeforeUpdate = storageShoesRepository.findAll().size();

        // Create the StorageShoes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStorageShoesMockMvc.perform(put("/api/storage-shoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageShoes)))
            .andExpect(status().isCreated());

        // Validate the StorageShoes in the database
        List<StorageShoes> storageShoesList = storageShoesRepository.findAll();
        assertThat(storageShoesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStorageShoes() throws Exception {
        // Initialize the database
        storageShoesRepository.saveAndFlush(storageShoes);
        int databaseSizeBeforeDelete = storageShoesRepository.findAll().size();

        // Get the storageShoes
        restStorageShoesMockMvc.perform(delete("/api/storage-shoes/{id}", storageShoes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StorageShoes> storageShoesList = storageShoesRepository.findAll();
        assertThat(storageShoesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageShoes.class);
        StorageShoes storageShoes1 = new StorageShoes();
        storageShoes1.setId(1L);
        StorageShoes storageShoes2 = new StorageShoes();
        storageShoes2.setId(storageShoes1.getId());
        assertThat(storageShoes1).isEqualTo(storageShoes2);
        storageShoes2.setId(2L);
        assertThat(storageShoes1).isNotEqualTo(storageShoes2);
        storageShoes1.setId(null);
        assertThat(storageShoes1).isNotEqualTo(storageShoes2);
    }
}
