package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.InfoPaid;
import com.mycompany.myapp.repository.InfoPaidRepository;
import com.mycompany.myapp.service.dto.InfoPaidDTO;
import com.mycompany.myapp.service.mapper.InfoPaidMapper;
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
 * Integration tests for the {@link InfoPaidResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfoPaidResourceIT {

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/info-paids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfoPaidRepository infoPaidRepository;

    @Autowired
    private InfoPaidMapper infoPaidMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfoPaidMockMvc;

    private InfoPaid infoPaid;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoPaid createEntity(EntityManager em) {
        InfoPaid infoPaid = new InfoPaid().expiryDate(DEFAULT_EXPIRY_DATE);
        return infoPaid;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoPaid createUpdatedEntity(EntityManager em) {
        InfoPaid infoPaid = new InfoPaid().expiryDate(UPDATED_EXPIRY_DATE);
        return infoPaid;
    }

    @BeforeEach
    public void initTest() {
        infoPaid = createEntity(em);
    }

    @Test
    @Transactional
    void createInfoPaid() throws Exception {
        int databaseSizeBeforeCreate = infoPaidRepository.findAll().size();
        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);
        restInfoPaidMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeCreate + 1);
        InfoPaid testInfoPaid = infoPaidList.get(infoPaidList.size() - 1);
        assertThat(testInfoPaid.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void createInfoPaidWithExistingId() throws Exception {
        // Create the InfoPaid with an existing ID
        infoPaid.setId(1L);
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        int databaseSizeBeforeCreate = infoPaidRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfoPaidMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInfoPaids() throws Exception {
        // Initialize the database
        infoPaidRepository.saveAndFlush(infoPaid);

        // Get all the infoPaidList
        restInfoPaidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infoPaid.getId().intValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }

    @Test
    @Transactional
    void getInfoPaid() throws Exception {
        // Initialize the database
        infoPaidRepository.saveAndFlush(infoPaid);

        // Get the infoPaid
        restInfoPaidMockMvc
            .perform(get(ENTITY_API_URL_ID, infoPaid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infoPaid.getId().intValue()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInfoPaid() throws Exception {
        // Get the infoPaid
        restInfoPaidMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfoPaid() throws Exception {
        // Initialize the database
        infoPaidRepository.saveAndFlush(infoPaid);

        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();

        // Update the infoPaid
        InfoPaid updatedInfoPaid = infoPaidRepository.findById(infoPaid.getId()).get();
        // Disconnect from session so that the updates on updatedInfoPaid are not directly saved in db
        em.detach(updatedInfoPaid);
        updatedInfoPaid.expiryDate(UPDATED_EXPIRY_DATE);
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(updatedInfoPaid);

        restInfoPaidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infoPaidDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isOk());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
        InfoPaid testInfoPaid = infoPaidList.get(infoPaidList.size() - 1);
        assertThat(testInfoPaid.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingInfoPaid() throws Exception {
        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();
        infoPaid.setId(count.incrementAndGet());

        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfoPaidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infoPaidDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfoPaid() throws Exception {
        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();
        infoPaid.setId(count.incrementAndGet());

        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoPaidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfoPaid() throws Exception {
        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();
        infoPaid.setId(count.incrementAndGet());

        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoPaidMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfoPaidWithPatch() throws Exception {
        // Initialize the database
        infoPaidRepository.saveAndFlush(infoPaid);

        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();

        // Update the infoPaid using partial update
        InfoPaid partialUpdatedInfoPaid = new InfoPaid();
        partialUpdatedInfoPaid.setId(infoPaid.getId());

        restInfoPaidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfoPaid.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfoPaid))
            )
            .andExpect(status().isOk());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
        InfoPaid testInfoPaid = infoPaidList.get(infoPaidList.size() - 1);
        assertThat(testInfoPaid.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInfoPaidWithPatch() throws Exception {
        // Initialize the database
        infoPaidRepository.saveAndFlush(infoPaid);

        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();

        // Update the infoPaid using partial update
        InfoPaid partialUpdatedInfoPaid = new InfoPaid();
        partialUpdatedInfoPaid.setId(infoPaid.getId());

        partialUpdatedInfoPaid.expiryDate(UPDATED_EXPIRY_DATE);

        restInfoPaidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfoPaid.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfoPaid))
            )
            .andExpect(status().isOk());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
        InfoPaid testInfoPaid = infoPaidList.get(infoPaidList.size() - 1);
        assertThat(testInfoPaid.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInfoPaid() throws Exception {
        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();
        infoPaid.setId(count.incrementAndGet());

        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfoPaidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infoPaidDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfoPaid() throws Exception {
        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();
        infoPaid.setId(count.incrementAndGet());

        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoPaidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfoPaid() throws Exception {
        int databaseSizeBeforeUpdate = infoPaidRepository.findAll().size();
        infoPaid.setId(count.incrementAndGet());

        // Create the InfoPaid
        InfoPaidDTO infoPaidDTO = infoPaidMapper.toDto(infoPaid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfoPaidMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infoPaidDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfoPaid in the database
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfoPaid() throws Exception {
        // Initialize the database
        infoPaidRepository.saveAndFlush(infoPaid);

        int databaseSizeBeforeDelete = infoPaidRepository.findAll().size();

        // Delete the infoPaid
        restInfoPaidMockMvc
            .perform(delete(ENTITY_API_URL_ID, infoPaid.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfoPaid> infoPaidList = infoPaidRepository.findAll();
        assertThat(infoPaidList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
