package head.web.rest;

import com.codahale.metrics.annotation.Timed;
import head.domain.Models;

import head.repository.ModelsRepository;
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
 * REST controller for managing Models.
 */
@RestController
@RequestMapping("/api")
public class ModelsResource {

    private final Logger log = LoggerFactory.getLogger(ModelsResource.class);

    private static final String ENTITY_NAME = "models";

    private final ModelsRepository modelsRepository;

    public ModelsResource(ModelsRepository modelsRepository) {
        this.modelsRepository = modelsRepository;
    }

    /**
     * POST  /models : Create a new models.
     *
     * @param models the models to create
     * @return the ResponseEntity with status 201 (Created) and with body the new models, or with status 400 (Bad Request) if the models has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/models")
    @Timed
    public ResponseEntity<Models> createModels(@Valid @RequestBody Models models) throws URISyntaxException {
        log.debug("REST request to save Models : {}", models);
        if (models.getId() != null) {
            throw new BadRequestAlertException("A new models cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Models result = modelsRepository.save(models);
        return ResponseEntity.created(new URI("/api/models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /models : Updates an existing models.
     *
     * @param models the models to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated models,
     * or with status 400 (Bad Request) if the models is not valid,
     * or with status 500 (Internal Server Error) if the models couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/models")
    @Timed
    public ResponseEntity<Models> updateModels(@Valid @RequestBody Models models) throws URISyntaxException {
        log.debug("REST request to update Models : {}", models);
        if (models.getId() == null) {
            return createModels(models);
        }
        Models result = modelsRepository.save(models);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, models.getId().toString()))
            .body(result);
    }

    /**
     * GET  /models : get all the models.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of models in body
     */
    @GetMapping("/models")
    @Timed
    public List<Models> getAllModels() {
        log.debug("REST request to get all Models");
        return modelsRepository.findAll();
        }

    /**
     * GET  /models/:id : get the "id" models.
     *
     * @param id the id of the models to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the models, or with status 404 (Not Found)
     */
    @GetMapping("/models/{id}")
    @Timed
    public ResponseEntity<Models> getModels(@PathVariable Long id) {
        log.debug("REST request to get Models : {}", id);
        Models models = modelsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(models));
    }

    /**
     * DELETE  /models/:id : delete the "id" models.
     *
     * @param id the id of the models to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/models/{id}")
    @Timed
    public ResponseEntity<Void> deleteModels(@PathVariable Long id) {
        log.debug("REST request to delete Models : {}", id);
        modelsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
