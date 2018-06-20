package head.web.rest;

import com.codahale.metrics.annotation.Timed;
import head.domain.Magazine;

import head.repository.MagazineRepository;
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
 * REST controller for managing Magazine.
 */
@RestController
@RequestMapping("/api")
public class MagazineResource {

    private final Logger log = LoggerFactory.getLogger(MagazineResource.class);

    private static final String ENTITY_NAME = "magazine";

    private final MagazineRepository magazineRepository;

    public MagazineResource(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    /**
     * POST  /magazines : Create a new magazine.
     *
     * @param magazine the magazine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new magazine, or with status 400 (Bad Request) if the magazine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/magazines")
    @Timed
    public ResponseEntity<Magazine> createMagazine(@Valid @RequestBody Magazine magazine) throws URISyntaxException {
        log.debug("REST request to save Magazine : {}", magazine);
        if (magazine.getId() != null) {
            throw new BadRequestAlertException("A new magazine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Magazine result = magazineRepository.save(magazine);
        return ResponseEntity.created(new URI("/api/magazines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /magazines : Updates an existing magazine.
     *
     * @param magazine the magazine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated magazine,
     * or with status 400 (Bad Request) if the magazine is not valid,
     * or with status 500 (Internal Server Error) if the magazine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/magazines")
    @Timed
    public ResponseEntity<Magazine> updateMagazine(@Valid @RequestBody Magazine magazine) throws URISyntaxException {
        log.debug("REST request to update Magazine : {}", magazine);
        if (magazine.getId() == null) {
            return createMagazine(magazine);
        }
        Magazine result = magazineRepository.save(magazine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, magazine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /magazines : get all the magazines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of magazines in body
     */
    @GetMapping("/magazines")
    @Timed
    public List<Magazine> getAllMagazines() {
        log.debug("REST request to get all Magazines");
        return magazineRepository.findAll();
        }

    /**
     * GET  /magazines/:id : get the "id" magazine.
     *
     * @param id the id of the magazine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the magazine, or with status 404 (Not Found)
     */
    @GetMapping("/magazines/{id}")
    @Timed
    public ResponseEntity<Magazine> getMagazine(@PathVariable Long id) {
        log.debug("REST request to get Magazine : {}", id);
        Magazine magazine = magazineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(magazine));
    }

    /**
     * DELETE  /magazines/:id : delete the "id" magazine.
     *
     * @param id the id of the magazine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/magazines/{id}")
    @Timed
    public ResponseEntity<Void> deleteMagazine(@PathVariable Long id) {
        log.debug("REST request to delete Magazine : {}", id);
        magazineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
