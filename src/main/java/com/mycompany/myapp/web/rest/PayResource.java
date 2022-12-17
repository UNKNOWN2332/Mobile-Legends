package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PayRepository;
import com.mycompany.myapp.service.PayService;
import com.mycompany.myapp.service.dto.PayDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Pay}.
 */
@RestController
@RequestMapping("/api")
public class PayResource {

    private final Logger log = LoggerFactory.getLogger(PayResource.class);

    private static final String ENTITY_NAME = "mobileLendsAppPay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayService payService;

    private final PayRepository payRepository;

    public PayResource(PayService payService, PayRepository payRepository) {
        this.payService = payService;
        this.payRepository = payRepository;
    }

    /**
     * {@code POST  /pays} : Create a new pay.
     *
     * @param payDTO the payDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payDTO, or with status {@code 400 (Bad Request)} if the pay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pays")
    public ResponseEntity<PayDTO> createPay(@Valid @RequestBody PayDTO payDTO) throws URISyntaxException {
        log.debug("REST request to save Pay : {}", payDTO);
        if (payDTO.getId() != null) {
            throw new BadRequestAlertException("A new pay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayDTO result = payService.save(payDTO);
        return ResponseEntity
            .created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pays/:id} : Updates an existing pay.
     *
     * @param id the id of the payDTO to save.
     * @param payDTO the payDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payDTO,
     * or with status {@code 400 (Bad Request)} if the payDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pays/{id}")
    public ResponseEntity<PayDTO> updatePay(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PayDTO payDTO)
        throws URISyntaxException {
        log.debug("REST request to update Pay : {}, {}", id, payDTO);
        if (payDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PayDTO result = payService.update(payDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pays/:id} : Partial updates given fields of an existing pay, field will ignore if it is null
     *
     * @param id the id of the payDTO to save.
     * @param payDTO the payDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payDTO,
     * or with status {@code 400 (Bad Request)} if the payDTO is not valid,
     * or with status {@code 404 (Not Found)} if the payDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the payDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PayDTO> partialUpdatePay(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PayDTO payDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pay partially : {}, {}", id, payDTO);
        if (payDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PayDTO> result = payService.partialUpdate(payDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pays} : get all the pays.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pays in body.
     */
    @GetMapping("/pays")
    public ResponseEntity<List<PayDTO>> getAllPays(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pays");
        Page<PayDTO> page = payService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pays/:id} : get the "id" pay.
     *
     * @param id the id of the payDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pays/{id}")
    public ResponseEntity<PayDTO> getPay(@PathVariable Long id) {
        log.debug("REST request to get Pay : {}", id);
        Optional<PayDTO> payDTO = payService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payDTO);
    }

    /**
     * {@code DELETE  /pays/:id} : delete the "id" pay.
     *
     * @param id the id of the payDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pays/{id}")
    public ResponseEntity<Void> deletePay(@PathVariable Long id) {
        log.debug("REST request to delete Pay : {}", id);
        payService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
