package head.web.rest;

import com.codahale.metrics.annotation.Timed;
import head.domain.GenericShoes;

import head.repository.GenericShoesRepository;
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
 * REST controller for managing GenericShoes.
 */
@RestController
@RequestMapping("/api")
public class GenericShoesResource {

    private final Logger log = LoggerFactory.getLogger(GenericShoesResource.class);

    private static final String ENTITY_NAME = "genericShoes";

    private final GenericShoesRepository genericShoesRepository;

    public GenericShoesResource(GenericShoesRepository genericShoesRepository) {
        this.genericShoesRepository = genericShoesRepository;
    }

    /**
     * POST  /generic-shoes : Create a new genericShoes.
     *
     * @param genericShoes the genericShoes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genericShoes, or with status 400 (Bad Request) if the genericShoes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/generic-shoes")
    @Timed
    public ResponseEntity<GenericShoes> createGenericShoes(@Valid @RequestBody GenericShoes genericShoes) throws URISyntaxException {
        log.debug("REST request to save GenericShoes : {}", genericShoes);
        if (genericShoes.getId() != null) {
            throw new BadRequestAlertException("A new genericShoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenericShoes result = genericShoesRepository.save(genericShoes);
        return ResponseEntity.created(new URI("/api/generic-shoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /generic-shoes : Updates an existing genericShoes.
     *
     * @param genericShoes the genericShoes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genericShoes,
     * or with status 400 (Bad Request) if the genericShoes is not valid,
     * or with status 500 (Internal Server Error) if the genericShoes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/generic-shoes")
    @Timed
    public ResponseEntity<GenericShoes> updateGenericShoes(@Valid @RequestBody GenericShoes genericShoes) throws URISyntaxException {
        log.debug("REST request to update GenericShoes : {}", genericShoes);
        if (genericShoes.getId() == null) {
            return createGenericShoes(genericShoes);
        }
        GenericShoes result = genericShoesRepository.save(genericShoes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, genericShoes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /generic-shoes : get all the genericShoes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of genericShoes in body
     */
    @GetMapping("/generic-shoes")
    @Timed
    public List<GenericShoes> getAllGenericShoes() {
        log.debug("REST request to get all GenericShoes");
        return genericShoesRepository.findAll();
        }

    /**
     * GET  /generic-shoes/:id : get the "id" genericShoes.
     *
     * @param id the id of the genericShoes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genericShoes, or with status 404 (Not Found)
     */
    @GetMapping("/generic-shoes/{id}")
    @Timed
    public ResponseEntity<GenericShoes> getGenericShoes(@PathVariable Long id) {
        log.debug("REST request to get GenericShoes : {}", id);
        GenericShoes genericShoes = genericShoesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(genericShoes));
    }

    /**
     * DELETE  /generic-shoes/:id : delete the "id" genericShoes.
     *
     * @param id the id of the genericShoes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/generic-shoes/{id}")
    @Timed
    public ResponseEntity<Void> deleteGenericShoes(@PathVariable Long id) {
        log.debug("REST request to delete GenericShoes : {}", id);
        genericShoesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
