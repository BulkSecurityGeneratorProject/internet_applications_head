package head.web.rest;

import head.HeadApp;

import head.domain.Models;
import head.repository.ModelsRepository;
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

import head.domain.enumeration.MODELTYPES;
/**
 * Test class for the ModelsResource REST controller.
 *
 * @see ModelsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HeadApp.class)
public class ModelsResourceIntTest {

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    private static final MODELTYPES DEFAULT_MODEL_TYPE = MODELTYPES.BOOTS;
    private static final MODELTYPES UPDATED_MODEL_TYPE = MODELTYPES.SANDALS;

    @Autowired
    private ModelsRepository modelsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModelsMockMvc;

    private Models models;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelsResource modelsResource = new ModelsResource(modelsRepository);
        this.restModelsMockMvc = MockMvcBuilders.standaloneSetup(modelsResource)
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
    public static Models createEntity(EntityManager em) {
        Models models = new Models()
            .modelName(DEFAULT_MODEL_NAME)
            .modelType(DEFAULT_MODEL_TYPE);
        return models;
    }

    @Before
    public void initTest() {
        models = createEntity(em);
    }

    @Test
    @Transactional
    public void createModels() throws Exception {
        int databaseSizeBeforeCreate = modelsRepository.findAll().size();

        // Create the Models
        restModelsMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(models)))
            .andExpect(status().isCreated());

        // Validate the Models in the database
        List<Models> modelsList = modelsRepository.findAll();
        assertThat(modelsList).hasSize(databaseSizeBeforeCreate + 1);
        Models testModels = modelsList.get(modelsList.size() - 1);
        assertThat(testModels.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
        assertThat(testModels.getModelType()).isEqualTo(DEFAULT_MODEL_TYPE);
    }

    @Test
    @Transactional
    public void createModelsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelsRepository.findAll().size();

        // Create the Models with an existing ID
        models.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelsMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(models)))
            .andExpect(status().isBadRequest());

        // Validate the Models in the database
        List<Models> modelsList = modelsRepository.findAll();
        assertThat(modelsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkModelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelsRepository.findAll().size();
        // set the field null
        models.setModelName(null);

        // Create the Models, which fails.

        restModelsMockMvc.perform(post("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(models)))
            .andExpect(status().isBadRequest());

        List<Models> modelsList = modelsRepository.findAll();
        assertThat(modelsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModels() throws Exception {
        // Initialize the database
        modelsRepository.saveAndFlush(models);

        // Get all the modelsList
        restModelsMockMvc.perform(get("/api/models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(models.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME.toString())))
            .andExpect(jsonPath("$.[*].modelType").value(hasItem(DEFAULT_MODEL_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getModels() throws Exception {
        // Initialize the database
        modelsRepository.saveAndFlush(models);

        // Get the models
        restModelsMockMvc.perform(get("/api/models/{id}", models.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(models.getId().intValue()))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME.toString()))
            .andExpect(jsonPath("$.modelType").value(DEFAULT_MODEL_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModels() throws Exception {
        // Get the models
        restModelsMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModels() throws Exception {
        // Initialize the database
        modelsRepository.saveAndFlush(models);
        int databaseSizeBeforeUpdate = modelsRepository.findAll().size();

        // Update the models
        Models updatedModels = modelsRepository.findOne(models.getId());
        // Disconnect from session so that the updates on updatedModels are not directly saved in db
        em.detach(updatedModels);
        updatedModels
            .modelName(UPDATED_MODEL_NAME)
            .modelType(UPDATED_MODEL_TYPE);

        restModelsMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModels)))
            .andExpect(status().isOk());

        // Validate the Models in the database
        List<Models> modelsList = modelsRepository.findAll();
        assertThat(modelsList).hasSize(databaseSizeBeforeUpdate);
        Models testModels = modelsList.get(modelsList.size() - 1);
        assertThat(testModels.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testModels.getModelType()).isEqualTo(UPDATED_MODEL_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingModels() throws Exception {
        int databaseSizeBeforeUpdate = modelsRepository.findAll().size();

        // Create the Models

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModelsMockMvc.perform(put("/api/models")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(models)))
            .andExpect(status().isCreated());

        // Validate the Models in the database
        List<Models> modelsList = modelsRepository.findAll();
        assertThat(modelsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModels() throws Exception {
        // Initialize the database
        modelsRepository.saveAndFlush(models);
        int databaseSizeBeforeDelete = modelsRepository.findAll().size();

        // Get the models
        restModelsMockMvc.perform(delete("/api/models/{id}", models.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Models> modelsList = modelsRepository.findAll();
        assertThat(modelsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Models.class);
        Models models1 = new Models();
        models1.setId(1L);
        Models models2 = new Models();
        models2.setId(models1.getId());
        assertThat(models1).isEqualTo(models2);
        models2.setId(2L);
        assertThat(models1).isNotEqualTo(models2);
        models1.setId(null);
        assertThat(models1).isNotEqualTo(models2);
    }
}
