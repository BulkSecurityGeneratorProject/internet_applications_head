package head.web.rest;

import com.codahale.metrics.annotation.Timed;
import head.domain.StorageShoes;

import head.repository.StorageShoesRepository;
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
 * REST controller for managing StorageShoes.
 */
@RestController
@RequestMapping("/api")
public class StorageShoesResource {

    private final Logger log = LoggerFactory.getLogger(StorageShoesResource.class);

    private static final String ENTITY_NAME = "storageShoes";

    private final StorageShoesRepository storageShoesRepository;

    public StorageShoesResource(StorageShoesRepository storageShoesRepository) {
        this.storageShoesRepository = storageShoesRepository;
    }

    /**
     * POST  /storage-shoes : Create a new storageShoes.
     *
     * @param storageShoes the storageShoes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storageShoes, or with status 400 (Bad Request) if the storageShoes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/storage-shoes")
    @Timed
    public ResponseEntity<StorageShoes> createStorageShoes(@Valid @RequestBody StorageShoes storageShoes) throws URISyntaxException {
        log.debug("REST request to save StorageShoes : {}", storageShoes);
        if (storageShoes.getId() != null) {
            throw new BadRequestAlertException("A new storageShoes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StorageShoes result = storageShoesRepository.save(storageShoes);
        return ResponseEntity.created(new URI("/api/storage-shoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /storage-shoes : Updates an existing storageShoes.
     *
     * @param storageShoes the storageShoes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storageShoes,
     * or with status 400 (Bad Request) if the storageShoes is not valid,
     * or with status 500 (Internal Server Error) if the storageShoes couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/storage-shoes")
    @Timed
    public ResponseEntity<StorageShoes> updateStorageShoes(@Valid @RequestBody StorageShoes storageShoes) throws URISyntaxException {
        log.debug("REST request to update StorageShoes : {}", storageShoes);
        if (storageShoes.getId() == null) {
            return createStorageShoes(storageShoes);
        }
        StorageShoes result = storageShoesRepository.save(storageShoes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storageShoes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /storage-shoes : get all the storageShoes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of storageShoes in body
     */
    @GetMapping("/storage-shoes")
    @Timed
    public List<StorageShoes> getAllStorageShoes() {
        log.debug("REST request to get all StorageShoes");
        return storageShoesRepository.findAll();
        }

    /**
     * GET  /storage-shoes/:id : get the "id" storageShoes.
     *
     * @param id the id of the storageShoes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storageShoes, or with status 404 (Not Found)
     */
    @GetMapping("/storage-shoes/{id}")
    @Timed
    public ResponseEntity<StorageShoes> getStorageShoes(@PathVariable Long id) {
        log.debug("REST request to get StorageShoes : {}", id);
        StorageShoes storageShoes = storageShoesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(storageShoes));
    }

    /**
     * DELETE  /storage-shoes/:id : delete the "id" storageShoes.
     *
     * @param id the id of the storageShoes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/storage-shoes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStorageShoes(@PathVariable Long id) {
        log.debug("REST request to delete StorageShoes : {}", id);
        storageShoesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
