package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Turnirs;
import com.mycompany.myapp.repository.TurnirsRepository;
import com.mycompany.myapp.service.dto.TurnirsDTO;
import com.mycompany.myapp.service.mapper.TurnirsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TurnirsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TurnirsResourceIT {

    private static final String DEFAULT_TURNIR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TURNIR_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATER_ID = 1L;
    private static final Long UPDATED_CREATER_ID = 2L;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final String ENTITY_API_URL = "/api/turnirs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TurnirsRepository turnirsRepository;

    @Autowired
    private TurnirsMapper turnirsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurnirsMockMvc;

    private Turnirs turnirs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turnirs createEntity(EntityManager em) {
        Turnirs turnirs = new Turnirs()
            .turnirName(DEFAULT_TURNIR_NAME)
            .createrId(DEFAULT_CREATER_ID)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .price(DEFAULT_PRICE);
        return turnirs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turnirs createUpdatedEntity(EntityManager em) {
        Turnirs turnirs = new Turnirs()
            .turnirName(UPDATED_TURNIR_NAME)
            .createrId(UPDATED_CREATER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .price(UPDATED_PRICE);
        return turnirs;
    }

    @BeforeEach
    public void initTest() {
        turnirs = createEntity(em);
    }

    @Test
    @Transactional
    void createTurnirs() throws Exception {
        int databaseSizeBeforeCreate = turnirsRepository.findAll().size();
        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);
        restTurnirsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeCreate + 1);
        Turnirs testTurnirs = turnirsList.get(turnirsList.size() - 1);
        assertThat(testTurnirs.getTurnirName()).isEqualTo(DEFAULT_TURNIR_NAME);
        assertThat(testTurnirs.getCreaterId()).isEqualTo(DEFAULT_CREATER_ID);
        assertThat(testTurnirs.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTurnirs.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTurnirs.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createTurnirsWithExistingId() throws Exception {
        // Create the Turnirs with an existing ID
        turnirs.setId(1L);
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        int databaseSizeBeforeCreate = turnirsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurnirsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTurnirNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = turnirsRepository.findAll().size();
        // set the field null
        turnirs.setTurnirName(null);

        // Create the Turnirs, which fails.
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        restTurnirsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurnirs() throws Exception {
        // Initialize the database
        turnirsRepository.saveAndFlush(turnirs);

        // Get all the turnirsList
        restTurnirsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnirs.getId().intValue())))
            .andExpect(jsonPath("$.[*].turnirName").value(hasItem(DEFAULT_TURNIR_NAME)))
            .andExpect(jsonPath("$.[*].createrId").value(hasItem(DEFAULT_CREATER_ID.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    void getTurnirs() throws Exception {
        // Initialize the database
        turnirsRepository.saveAndFlush(turnirs);

        // Get the turnirs
        restTurnirsMockMvc
            .perform(get(ENTITY_API_URL_ID, turnirs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turnirs.getId().intValue()))
            .andExpect(jsonPath("$.turnirName").value(DEFAULT_TURNIR_NAME))
            .andExpect(jsonPath("$.createrId").value(DEFAULT_CREATER_ID.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTurnirs() throws Exception {
        // Get the turnirs
        restTurnirsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTurnirs() throws Exception {
        // Initialize the database
        turnirsRepository.saveAndFlush(turnirs);

        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();

        // Update the turnirs
        Turnirs updatedTurnirs = turnirsRepository.findById(turnirs.getId()).get();
        // Disconnect from session so that the updates on updatedTurnirs are not directly saved in db
        em.detach(updatedTurnirs);
        updatedTurnirs
            .turnirName(UPDATED_TURNIR_NAME)
            .createrId(UPDATED_CREATER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .price(UPDATED_PRICE);
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(updatedTurnirs);

        restTurnirsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnirsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
        Turnirs testTurnirs = turnirsList.get(turnirsList.size() - 1);
        assertThat(testTurnirs.getTurnirName()).isEqualTo(UPDATED_TURNIR_NAME);
        assertThat(testTurnirs.getCreaterId()).isEqualTo(UPDATED_CREATER_ID);
        assertThat(testTurnirs.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTurnirs.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTurnirs.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingTurnirs() throws Exception {
        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();
        turnirs.setId(count.incrementAndGet());

        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnirsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnirsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurnirs() throws Exception {
        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();
        turnirs.setId(count.incrementAndGet());

        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnirsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurnirs() throws Exception {
        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();
        turnirs.setId(count.incrementAndGet());

        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnirsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurnirsWithPatch() throws Exception {
        // Initialize the database
        turnirsRepository.saveAndFlush(turnirs);

        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();

        // Update the turnirs using partial update
        Turnirs partialUpdatedTurnirs = new Turnirs();
        partialUpdatedTurnirs.setId(turnirs.getId());

        partialUpdatedTurnirs.createrId(UPDATED_CREATER_ID);

        restTurnirsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurnirs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurnirs))
            )
            .andExpect(status().isOk());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
        Turnirs testTurnirs = turnirsList.get(turnirsList.size() - 1);
        assertThat(testTurnirs.getTurnirName()).isEqualTo(DEFAULT_TURNIR_NAME);
        assertThat(testTurnirs.getCreaterId()).isEqualTo(UPDATED_CREATER_ID);
        assertThat(testTurnirs.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTurnirs.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTurnirs.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateTurnirsWithPatch() throws Exception {
        // Initialize the database
        turnirsRepository.saveAndFlush(turnirs);

        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();

        // Update the turnirs using partial update
        Turnirs partialUpdatedTurnirs = new Turnirs();
        partialUpdatedTurnirs.setId(turnirs.getId());

        partialUpdatedTurnirs
            .turnirName(UPDATED_TURNIR_NAME)
            .createrId(UPDATED_CREATER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .price(UPDATED_PRICE);

        restTurnirsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurnirs.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurnirs))
            )
            .andExpect(status().isOk());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
        Turnirs testTurnirs = turnirsList.get(turnirsList.size() - 1);
        assertThat(testTurnirs.getTurnirName()).isEqualTo(UPDATED_TURNIR_NAME);
        assertThat(testTurnirs.getCreaterId()).isEqualTo(UPDATED_CREATER_ID);
        assertThat(testTurnirs.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTurnirs.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTurnirs.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingTurnirs() throws Exception {
        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();
        turnirs.setId(count.incrementAndGet());

        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnirsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turnirsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurnirs() throws Exception {
        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();
        turnirs.setId(count.incrementAndGet());

        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnirsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurnirs() throws Exception {
        int databaseSizeBeforeUpdate = turnirsRepository.findAll().size();
        turnirs.setId(count.incrementAndGet());

        // Create the Turnirs
        TurnirsDTO turnirsDTO = turnirsMapper.toDto(turnirs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnirsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turnirsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turnirs in the database
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurnirs() throws Exception {
        // Initialize the database
        turnirsRepository.saveAndFlush(turnirs);

        int databaseSizeBeforeDelete = turnirsRepository.findAll().size();

        // Delete the turnirs
        restTurnirsMockMvc
            .perform(delete(ENTITY_API_URL_ID, turnirs.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Turnirs> turnirsList = turnirsRepository.findAll();
        assertThat(turnirsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
