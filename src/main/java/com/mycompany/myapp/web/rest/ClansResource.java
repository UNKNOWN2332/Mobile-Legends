package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ClansRepository;
import com.mycompany.myapp.service.ClansService;
import com.mycompany.myapp.service.dto.ClansDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Clans}.
 */
@RestController
@RequestMapping("/api")
public class ClansResource {

    private final Logger log = LoggerFactory.getLogger(ClansResource.class);

    private static final String ENTITY_NAME = "mobileLendsAppClans";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClansService clansService;

    private final ClansRepository clansRepository;

    public ClansResource(ClansService clansService, ClansRepository clansRepository) {
        this.clansService = clansService;
        this.clansRepository = clansRepository;
    }

    /**
     * {@code POST  /clans} : Create a new clans.
     *
     * @param clansDTO the clansDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clansDTO, or with status {@code 400 (Bad Request)} if the clans has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clans")
    public ResponseEntity<ClansDTO> createClans(@Valid @RequestBody ClansDTO clansDTO) throws URISyntaxException {
        log.debug("REST request to save Clans : {}", clansDTO);
        if (clansDTO.getId() != null) {
            throw new BadRequestAlertException("A new clans cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClansDTO result = clansService.save(clansDTO);
        return ResponseEntity
            .created(new URI("/api/clans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clans/:id} : Updates an existing clans.
     *
     * @param id the id of the clansDTO to save.
     * @param clansDTO the clansDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clansDTO,
     * or with status {@code 400 (Bad Request)} if the clansDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clansDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clans/{id}")
    public ResponseEntity<ClansDTO> updateClans(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClansDTO clansDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Clans : {}, {}", id, clansDTO);
        if (clansDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clansDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClansDTO result = clansService.update(clansDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clansDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clans/:id} : Partial updates given fields of an existing clans, field will ignore if it is null
     *
     * @param id the id of the clansDTO to save.
     * @param clansDTO the clansDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clansDTO,
     * or with status {@code 400 (Bad Request)} if the clansDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clansDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clansDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClansDTO> partialUpdateClans(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClansDTO clansDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clans partially : {}, {}", id, clansDTO);
        if (clansDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clansDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClansDTO> result = clansService.partialUpdate(clansDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clansDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clans} : get all the clans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clans in body.
     */
    @GetMapping("/clans")
    public ResponseEntity<List<ClansDTO>> getAllClans(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Clans");
        Page<ClansDTO> page = clansService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clans/:id} : get the "id" clans.
     *
     * @param id the id of the clansDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clansDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clans/{id}")
    public ResponseEntity<ClansDTO> getClans(@PathVariable Long id) {
        log.debug("REST request to get Clans : {}", id);
        Optional<ClansDTO> clansDTO = clansService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clansDTO);
    }

    /**
     * {@code DELETE  /clans/:id} : delete the "id" clans.
     *
     * @param id the id of the clansDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clans/{id}")
    public ResponseEntity<Void> deleteClans(@PathVariable Long id) {
        log.debug("REST request to delete Clans : {}", id);
        clansService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
