package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Period;
import com.mycompany.myapp.domain.enumeration.DatesOfPeriod;
import com.mycompany.myapp.repository.PeriodRepository;
import com.mycompany.myapp.service.dto.PeriodDTO;
import com.mycompany.myapp.service.mapper.PeriodMapper;
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
 * Integration tests for the {@link PeriodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeriodResourceIT {

    private static final String DEFAULT_PERIOD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final DatesOfPeriod DEFAULT_DATES_OF_PERIOD = DatesOfPeriod.Year;
    private static final DatesOfPeriod UPDATED_DATES_OF_PERIOD = DatesOfPeriod.HalfYear;

    private static final String ENTITY_API_URL = "/api/periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private PeriodMapper periodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodMockMvc;

    private Period period;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createEntity(EntityManager em) {
        Period period = new Period()
            .periodName(DEFAULT_PERIOD_NAME)
            .createAt(DEFAULT_CREATE_AT)
            .amount(DEFAULT_AMOUNT)
            .datesOfPeriod(DEFAULT_DATES_OF_PERIOD);
        return period;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createUpdatedEntity(EntityManager em) {
        Period period = new Period()
            .periodName(UPDATED_PERIOD_NAME)
            .createAt(UPDATED_CREATE_AT)
            .amount(UPDATED_AMOUNT)
            .datesOfPeriod(UPDATED_DATES_OF_PERIOD);
        return period;
    }

    @BeforeEach
    public void initTest() {
        period = createEntity(em);
    }

    @Test
    @Transactional
    void createPeriod() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();
        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);
        restPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate + 1);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getPeriodName()).isEqualTo(DEFAULT_PERIOD_NAME);
        assertThat(testPeriod.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testPeriod.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPeriod.getDatesOfPeriod()).isEqualTo(DEFAULT_DATES_OF_PERIOD);
    }

    @Test
    @Transactional
    void createPeriodWithExistingId() throws Exception {
        // Create the Period with an existing ID
        period.setId(1L);
        PeriodDTO periodDTO = periodMapper.toDto(period);

        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeriods() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList
        restPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodName").value(hasItem(DEFAULT_PERIOD_NAME)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].datesOfPeriod").value(hasItem(DEFAULT_DATES_OF_PERIOD.toString())));
    }

    @Test
    @Transactional
    void getPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get the period
        restPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, period.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(period.getId().intValue()))
            .andExpect(jsonPath("$.periodName").value(DEFAULT_PERIOD_NAME))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.datesOfPeriod").value(DEFAULT_DATES_OF_PERIOD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPeriod() throws Exception {
        // Get the period
        restPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period
        Period updatedPeriod = periodRepository.findById(period.getId()).get();
        // Disconnect from session so that the updates on updatedPeriod are not directly saved in db
        em.detach(updatedPeriod);
        updatedPeriod
            .periodName(UPDATED_PERIOD_NAME)
            .createAt(UPDATED_CREATE_AT)
            .amount(UPDATED_AMOUNT)
            .datesOfPeriod(UPDATED_DATES_OF_PERIOD);
        PeriodDTO periodDTO = periodMapper.toDto(updatedPeriod);

        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periodDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getPeriodName()).isEqualTo(UPDATED_PERIOD_NAME);
        assertThat(testPeriod.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPeriod.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPeriod.getDatesOfPeriod()).isEqualTo(UPDATED_DATES_OF_PERIOD);
    }

    @Test
    @Transactional
    void putNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periodDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeriodWithPatch() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period using partial update
        Period partialUpdatedPeriod = new Period();
        partialUpdatedPeriod.setId(period.getId());

        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriod.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getPeriodName()).isEqualTo(DEFAULT_PERIOD_NAME);
        assertThat(testPeriod.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testPeriod.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPeriod.getDatesOfPeriod()).isEqualTo(DEFAULT_DATES_OF_PERIOD);
    }

    @Test
    @Transactional
    void fullUpdatePeriodWithPatch() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period using partial update
        Period partialUpdatedPeriod = new Period();
        partialUpdatedPeriod.setId(period.getId());

        partialUpdatedPeriod
            .periodName(UPDATED_PERIOD_NAME)
            .createAt(UPDATED_CREATE_AT)
            .amount(UPDATED_AMOUNT)
            .datesOfPeriod(UPDATED_DATES_OF_PERIOD);

        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriod.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getPeriodName()).isEqualTo(UPDATED_PERIOD_NAME);
        assertThat(testPeriod.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPeriod.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPeriod.getDatesOfPeriod()).isEqualTo(UPDATED_DATES_OF_PERIOD);
    }

    @Test
    @Transactional
    void patchNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, periodDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();
        period.setId(count.incrementAndGet());

        // Create the Period
        PeriodDTO periodDTO = periodMapper.toDto(period);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(periodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        int databaseSizeBeforeDelete = periodRepository.findAll().size();

        // Delete the period
        restPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, period.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
