package head.web.rest;

import head.HeadApp;

import head.domain.Employees;
import head.repository.EmployeesRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static head.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EmployeesResource REST controller.
 *
 * @see EmployeesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HeadApp.class)
public class EmployeesResourceIntTest {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_SALARY = 1F;
    private static final Float UPDATED_SALARY = 2F;

    private static final String DEFAULT_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH = "BBBBBBBBBB";

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeesMockMvc;

    private Employees employees;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeesResource employeesResource = new EmployeesResource(employeesRepository);
        this.restEmployeesMockMvc = MockMvcBuilders.standaloneSetup(employeesResource)
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
    public static Employees createEntity(EntityManager em) {
        Employees employees = new Employees()
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .position(DEFAULT_POSITION)
            .email(DEFAULT_EMAIL)
            .active(DEFAULT_ACTIVE)
            .createDate(DEFAULT_CREATE_DATE)
            .salary(DEFAULT_SALARY)
            .branch(DEFAULT_BRANCH);
        return employees;
    }

    @Before
    public void initTest() {
        employees = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployees() throws Exception {
        int databaseSizeBeforeCreate = employeesRepository.findAll().size();

        // Create the Employees
        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isCreated());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeCreate + 1);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testEmployees.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testEmployees.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployees.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testEmployees.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployees.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testEmployees.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testEmployees.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployees.getBranch()).isEqualTo(DEFAULT_BRANCH);
    }

    @Test
    @Transactional
    public void createEmployeesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeesRepository.findAll().size();

        // Create the Employees with an existing ID
        employees.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setLogin(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setPassword(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setFirstName(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setLastName(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setCreateDate(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBranchIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeesRepository.findAll().size();
        // set the field null
        employees.setBranch(null);

        // Create the Employees, which fails.

        restEmployeesMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isBadRequest());

        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList
        restEmployeesMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employees.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].branch").value(hasItem(DEFAULT_BRANCH.toString())));
    }

    @Test
    @Transactional
    public void getEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get the employees
        restEmployeesMockMvc.perform(get("/api/employees/{id}", employees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employees.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.branch").value(DEFAULT_BRANCH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployees() throws Exception {
        // Get the employees
        restEmployeesMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Update the employees
        Employees updatedEmployees = employeesRepository.findOne(employees.getId());
        // Disconnect from session so that the updates on updatedEmployees are not directly saved in db
        em.detach(updatedEmployees);
        updatedEmployees
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .position(UPDATED_POSITION)
            .email(UPDATED_EMAIL)
            .active(UPDATED_ACTIVE)
            .createDate(UPDATED_CREATE_DATE)
            .salary(UPDATED_SALARY)
            .branch(UPDATED_BRANCH);

        restEmployeesMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployees)))
            .andExpect(status().isOk());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testEmployees.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testEmployees.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployees.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testEmployees.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployees.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testEmployees.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testEmployees.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployees.getBranch()).isEqualTo(UPDATED_BRANCH);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Create the Employees

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeesMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employees)))
            .andExpect(status().isCreated());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);
        int databaseSizeBeforeDelete = employeesRepository.findAll().size();

        // Get the employees
        restEmployeesMockMvc.perform(delete("/api/employees/{id}", employees.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employees.class);
        Employees employees1 = new Employees();
        employees1.setId(1L);
        Employees employees2 = new Employees();
        employees2.setId(employees1.getId());
        assertThat(employees1).isEqualTo(employees2);
        employees2.setId(2L);
        assertThat(employees1).isNotEqualTo(employees2);
        employees1.setId(null);
        assertThat(employees1).isNotEqualTo(employees2);
    }
}
