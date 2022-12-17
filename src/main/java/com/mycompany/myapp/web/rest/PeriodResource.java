package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PeriodRepository;
import com.mycompany.myapp.service.PeriodService;
import com.mycompany.myapp.service.dto.PeriodDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Period}.
 */
@RestController
@RequestMapping("/api")
public class PeriodResource {

    private final Logger log = LoggerFactory.getLogger(PeriodResource.class);

    private static final String ENTITY_NAME = "mobileLendsAppPeriod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodService periodService;

    private final PeriodRepository periodRepository;

    public PeriodResource(PeriodService periodService, PeriodRepository periodRepository) {
        this.periodService = periodService;
        this.periodRepository = periodRepository;
    }

    /**
     * {@code POST  /periods} : Create a new period.
     *
     * @param periodDTO the periodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodDTO, or with status {@code 400 (Bad Request)} if the period has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periods")
    public ResponseEntity<PeriodDTO> createPeriod(@Valid @RequestBody PeriodDTO periodDTO) throws URISyntaxException {
        log.debug("REST request to save Period : {}", periodDTO);
        if (periodDTO.getId() != null) {
            throw new BadRequestAlertException("A new period cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodDTO result = periodService.save(periodDTO);
        return ResponseEntity
            .created(new URI("/api/periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /periods/:id} : Updates an existing period.
     *
     * @param id the id of the periodDTO to save.
     * @param periodDTO the periodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodDTO,
     * or with status {@code 400 (Bad Request)} if the periodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periods/{id}")
    public ResponseEntity<PeriodDTO> updatePeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeriodDTO periodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Period : {}, {}", id, periodDTO);
        if (periodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PeriodDTO result = periodService.update(periodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /periods/:id} : Partial updates given fields of an existing period, field will ignore if it is null
     *
     * @param id the id of the periodDTO to save.
     * @param periodDTO the periodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodDTO,
     * or with status {@code 400 (Bad Request)} if the periodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the periodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the periodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/periods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeriodDTO> partialUpdatePeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeriodDTO periodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Period partially : {}, {}", id, periodDTO);
        if (periodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeriodDTO> result = periodService.partialUpdate(periodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /periods} : get all the periods.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periods in body.
     */
    @GetMapping("/periods")
    public ResponseEntity<List<PeriodDTO>> getAllPeriods(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Periods");
        Page<PeriodDTO> page = periodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /periods/:id} : get the "id" period.
     *
     * @param id the id of the periodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/periods/{id}")
    public ResponseEntity<PeriodDTO> getPeriod(@PathVariable Long id) {
        log.debug("REST request to get Period : {}", id);
        Optional<PeriodDTO> periodDTO = periodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodDTO);
    }

    /**
     * {@code DELETE  /periods/:id} : delete the "id" period.
     *
     * @param id the id of the periodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/periods/{id}")
    public ResponseEntity<Void> deletePeriod(@PathVariable Long id) {
        log.debug("REST request to delete Period : {}", id);
        periodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
