package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TelegramAccountRepository;
import com.mycompany.myapp.service.TelegramAccountService;
import com.mycompany.myapp.service.dto.TelegramAccountDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TelegramAccount}.
 */
@RestController
@RequestMapping("/api")
public class TelegramAccountResource {

    private final Logger log = LoggerFactory.getLogger(TelegramAccountResource.class);

    private static final String ENTITY_NAME = "mobileLendsAppTelegramAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TelegramAccountService telegramAccountService;

    private final TelegramAccountRepository telegramAccountRepository;

    public TelegramAccountResource(TelegramAccountService telegramAccountService, TelegramAccountRepository telegramAccountRepository) {
        this.telegramAccountService = telegramAccountService;
        this.telegramAccountRepository = telegramAccountRepository;
    }

    /**
     * {@code POST  /telegram-accounts} : Create a new telegramAccount.
     *
     * @param telegramAccountDTO the telegramAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new telegramAccountDTO, or with status {@code 400 (Bad Request)} if the telegramAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/telegram-accounts")
    public ResponseEntity<TelegramAccountDTO> createTelegramAccount(@Valid @RequestBody TelegramAccountDTO telegramAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save TelegramAccount : {}", telegramAccountDTO);
        if (telegramAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new telegramAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TelegramAccountDTO result = telegramAccountService.save(telegramAccountDTO);
        return ResponseEntity
            .created(new URI("/api/telegram-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /telegram-accounts/:id} : Updates an existing telegramAccount.
     *
     * @param id the id of the telegramAccountDTO to save.
     * @param telegramAccountDTO the telegramAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telegramAccountDTO,
     * or with status {@code 400 (Bad Request)} if the telegramAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the telegramAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/telegram-accounts/{id}")
    public ResponseEntity<TelegramAccountDTO> updateTelegramAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TelegramAccountDTO telegramAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TelegramAccount : {}, {}", id, telegramAccountDTO);
        if (telegramAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, telegramAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!telegramAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TelegramAccountDTO result = telegramAccountService.update(telegramAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, telegramAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /telegram-accounts/:id} : Partial updates given fields of an existing telegramAccount, field will ignore if it is null
     *
     * @param id the id of the telegramAccountDTO to save.
     * @param telegramAccountDTO the telegramAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated telegramAccountDTO,
     * or with status {@code 400 (Bad Request)} if the telegramAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the telegramAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the telegramAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/telegram-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TelegramAccountDTO> partialUpdateTelegramAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TelegramAccountDTO telegramAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TelegramAccount partially : {}, {}", id, telegramAccountDTO);
        if (telegramAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, telegramAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!telegramAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TelegramAccountDTO> result = telegramAccountService.partialUpdate(telegramAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, telegramAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /telegram-accounts} : get all the telegramAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of telegramAccounts in body.
     */
    @GetMapping("/telegram-accounts")
    public ResponseEntity<List<TelegramAccountDTO>> getAllTelegramAccounts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TelegramAccounts");
        Page<TelegramAccountDTO> page = telegramAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /telegram-accounts/:id} : get the "id" telegramAccount.
     *
     * @param id the id of the telegramAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the telegramAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/telegram-accounts/{id}")
    public ResponseEntity<TelegramAccountDTO> getTelegramAccount(@PathVariable Long id) {
        log.debug("REST request to get TelegramAccount : {}", id);
        Optional<TelegramAccountDTO> telegramAccountDTO = telegramAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(telegramAccountDTO);
    }

    /**
     * {@code DELETE  /telegram-accounts/:id} : delete the "id" telegramAccount.
     *
     * @param id the id of the telegramAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/telegram-accounts/{id}")
    public ResponseEntity<Void> deleteTelegramAccount(@PathVariable Long id) {
        log.debug("REST request to delete TelegramAccount : {}", id);
        telegramAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
