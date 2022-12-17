package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TurnirsRepository;
import com.mycompany.myapp.service.TurnirsService;
import com.mycompany.myapp.service.dto.TurnirsDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Turnirs}.
 */
@RestController
@RequestMapping("/api")
public class TurnirsResource {

    private final Logger log = LoggerFactory.getLogger(TurnirsResource.class);

    private static final String ENTITY_NAME = "mobileLendsAppTurnirs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TurnirsService turnirsService;

    private final TurnirsRepository turnirsRepository;

    public TurnirsResource(TurnirsService turnirsService, TurnirsRepository turnirsRepository) {
        this.turnirsService = turnirsService;
        this.turnirsRepository = turnirsRepository;
    }

    /**
     * {@code POST  /turnirs} : Create a new turnirs.
     *
     * @param turnirsDTO the turnirsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turnirsDTO, or with status {@code 400 (Bad Request)} if the turnirs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/turnirs")
    public ResponseEntity<TurnirsDTO> createTurnirs(@Valid @RequestBody TurnirsDTO turnirsDTO) throws URISyntaxException {
        log.debug("REST request to save Turnirs : {}", turnirsDTO);
        if (turnirsDTO.getId() != null) {
            throw new BadRequestAlertException("A new turnirs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurnirsDTO result = turnirsService.save(turnirsDTO);
        return ResponseEntity
            .created(new URI("/api/turnirs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /turnirs/:id} : Updates an existing turnirs.
     *
     * @param id the id of the turnirsDTO to save.
     * @param turnirsDTO the turnirsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnirsDTO,
     * or with status {@code 400 (Bad Request)} if the turnirsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turnirsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/turnirs/{id}")
    public ResponseEntity<TurnirsDTO> updateTurnirs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TurnirsDTO turnirsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Turnirs : {}, {}", id, turnirsDTO);
        if (turnirsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnirsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnirsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TurnirsDTO result = turnirsService.update(turnirsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turnirsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /turnirs/:id} : Partial updates given fields of an existing turnirs, field will ignore if it is null
     *
     * @param id the id of the turnirsDTO to save.
     * @param turnirsDTO the turnirsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnirsDTO,
     * or with status {@code 400 (Bad Request)} if the turnirsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the turnirsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the turnirsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/turnirs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TurnirsDTO> partialUpdateTurnirs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TurnirsDTO turnirsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Turnirs partially : {}, {}", id, turnirsDTO);
        if (turnirsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnirsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnirsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TurnirsDTO> result = turnirsService.partialUpdate(turnirsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turnirsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /turnirs} : get all the turnirs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of turnirs in body.
     */
    @GetMapping("/turnirs")
    public ResponseEntity<List<TurnirsDTO>> getAllTurnirs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Turnirs");
        Page<TurnirsDTO> page = turnirsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /turnirs/:id} : get the "id" turnirs.
     *
     * @param id the id of the turnirsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turnirsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/turnirs/{id}")
    public ResponseEntity<TurnirsDTO> getTurnirs(@PathVariable Long id) {
        log.debug("REST request to get Turnirs : {}", id);
        Optional<TurnirsDTO> turnirsDTO = turnirsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turnirsDTO);
    }

    /**
     * {@code DELETE  /turnirs/:id} : delete the "id" turnirs.
     *
     * @param id the id of the turnirsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/turnirs/{id}")
    public ResponseEntity<Void> deleteTurnirs(@PathVariable Long id) {
        log.debug("REST request to delete Turnirs : {}", id);
        turnirsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
