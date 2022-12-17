package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.InfoPaidRepository;
import com.mycompany.myapp.service.InfoPaidService;
import com.mycompany.myapp.service.dto.InfoPaidDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.InfoPaid}.
 */
@RestController
@RequestMapping("/api")
public class InfoPaidResource {

    private final Logger log = LoggerFactory.getLogger(InfoPaidResource.class);

    private static final String ENTITY_NAME = "mobileLendsAppInfoPaid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfoPaidService infoPaidService;

    private final InfoPaidRepository infoPaidRepository;

    public InfoPaidResource(InfoPaidService infoPaidService, InfoPaidRepository infoPaidRepository) {
        this.infoPaidService = infoPaidService;
        this.infoPaidRepository = infoPaidRepository;
    }

    /**
     * {@code POST  /info-paids} : Create a new infoPaid.
     *
     * @param infoPaidDTO the infoPaidDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infoPaidDTO, or with status {@code 400 (Bad Request)} if the infoPaid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/info-paids")
    public ResponseEntity<InfoPaidDTO> createInfoPaid(@RequestBody InfoPaidDTO infoPaidDTO) throws URISyntaxException {
        log.debug("REST request to save InfoPaid : {}", infoPaidDTO);
        if (infoPaidDTO.getId() != null) {
            throw new BadRequestAlertException("A new infoPaid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfoPaidDTO result = infoPaidService.save(infoPaidDTO);
        return ResponseEntity
            .created(new URI("/api/info-paids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /info-paids/:id} : Updates an existing infoPaid.
     *
     * @param id the id of the infoPaidDTO to save.
     * @param infoPaidDTO the infoPaidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoPaidDTO,
     * or with status {@code 400 (Bad Request)} if the infoPaidDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infoPaidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/info-paids/{id}")
    public ResponseEntity<InfoPaidDTO> updateInfoPaid(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InfoPaidDTO infoPaidDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InfoPaid : {}, {}", id, infoPaidDTO);
        if (infoPaidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoPaidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infoPaidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfoPaidDTO result = infoPaidService.update(infoPaidDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infoPaidDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /info-paids/:id} : Partial updates given fields of an existing infoPaid, field will ignore if it is null
     *
     * @param id the id of the infoPaidDTO to save.
     * @param infoPaidDTO the infoPaidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoPaidDTO,
     * or with status {@code 400 (Bad Request)} if the infoPaidDTO is not valid,
     * or with status {@code 404 (Not Found)} if the infoPaidDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the infoPaidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/info-paids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfoPaidDTO> partialUpdateInfoPaid(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InfoPaidDTO infoPaidDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfoPaid partially : {}, {}", id, infoPaidDTO);
        if (infoPaidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoPaidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infoPaidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfoPaidDTO> result = infoPaidService.partialUpdate(infoPaidDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infoPaidDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /info-paids} : get all the infoPaids.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infoPaids in body.
     */
    @GetMapping("/info-paids")
    public ResponseEntity<List<InfoPaidDTO>> getAllInfoPaids(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of InfoPaids");
        Page<InfoPaidDTO> page = infoPaidService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /info-paids/:id} : get the "id" infoPaid.
     *
     * @param id the id of the infoPaidDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infoPaidDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/info-paids/{id}")
    public ResponseEntity<InfoPaidDTO> getInfoPaid(@PathVariable Long id) {
        log.debug("REST request to get InfoPaid : {}", id);
        Optional<InfoPaidDTO> infoPaidDTO = infoPaidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infoPaidDTO);
    }

    /**
     * {@code DELETE  /info-paids/:id} : delete the "id" infoPaid.
     *
     * @param id the id of the infoPaidDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/info-paids/{id}")
    public ResponseEntity<Void> deleteInfoPaid(@PathVariable Long id) {
        log.debug("REST request to delete InfoPaid : {}", id);
        infoPaidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
