package head.web.rest;

import com.codahale.metrics.annotation.Timed;
import head.domain.Employees;

import head.repository.EmployeesRepository;
import head.web.rest.errors.BadRequestAlertException;
import head.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Employees.
 */
@RestController
@RequestMapping("/api")
public class EmployeesResource {

    private final Logger log = LoggerFactory.getLogger(EmployeesResource.class);

    private static final String ENTITY_NAME = "employees";

    private final EmployeesRepository employeesRepository;

    public EmployeesResource(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    /**
     * POST  /employees : Create a new employees.
     *
     * @param employees the employees to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employees, or with status 400 (Bad Request) if the employees has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employees")
    @Timed
    public ResponseEntity<Employees> createEmployees(@Valid @RequestBody Employees employees) throws URISyntaxException {
        log.debug("REST request to save Employees : {}", employees);
        if (employees.getId() != null) {
            throw new BadRequestAlertException("A new employees cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employees result = employeesRepository.save(employees);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees : Updates an existing employees.
     *
     * @param employees the employees to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employees,
     * or with status 400 (Bad Request) if the employees is not valid,
     * or with status 500 (Internal Server Error) if the employees couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employees")
    @Timed
    public ResponseEntity<Employees> updateEmployees(@Valid @RequestBody Employees employees) throws URISyntaxException {
        log.debug("REST request to update Employees : {}", employees);
        if (employees.getId() == null) {
            return createEmployees(employees);
        }
        Employees result = employeesRepository.save(employees);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employees.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees : get all the employees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     */
    @GetMapping("/employees")
    @Timed
    public List<Employees> getAllEmployees() {
        log.debug("REST request to get all Employees");
        return employeesRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /employees/:id : get the "id" employees.
     *
     * @param id the id of the employees to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employees, or with status 404 (Not Found)
     */
    @GetMapping("/employees/{id}")
    @Timed
    public ResponseEntity<Employees> getEmployees(@PathVariable Long id) {
        log.debug("REST request to get Employees : {}", id);
        Employees employees = employeesRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(employees));
    }

    /**
     * DELETE  /employees/:id : delete the "id" employees.
     *
     * @param id the id of the employees to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employees/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmployees(@PathVariable Long id) {
        log.debug("REST request to delete Employees : {}", id);
        employeesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
