package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Clans;
import com.mycompany.myapp.repository.ClansRepository;
import com.mycompany.myapp.service.dto.ClansDTO;
import com.mycompany.myapp.service.mapper.ClansMapper;
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
 * Integration tests for the {@link ClansResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClansResourceIT {

    private static final String DEFAULT_CLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLAN_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CAPITAN_ID = 1L;
    private static final Long UPDATED_CAPITAN_ID = 2L;

    private static final String ENTITY_API_URL = "/api/clans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClansRepository clansRepository;

    @Autowired
    private ClansMapper clansMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClansMockMvc;

    private Clans clans;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clans createEntity(EntityManager em) {
        Clans clans = new Clans().clanName(DEFAULT_CLAN_NAME).capitanId(DEFAULT_CAPITAN_ID);
        return clans;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clans createUpdatedEntity(EntityManager em) {
        Clans clans = new Clans().clanName(UPDATED_CLAN_NAME).capitanId(UPDATED_CAPITAN_ID);
        return clans;
    }

    @BeforeEach
    public void initTest() {
        clans = createEntity(em);
    }

    @Test
    @Transactional
    void createClans() throws Exception {
        int databaseSizeBeforeCreate = clansRepository.findAll().size();
        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);
        restClansMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeCreate + 1);
        Clans testClans = clansList.get(clansList.size() - 1);
        assertThat(testClans.getClanName()).isEqualTo(DEFAULT_CLAN_NAME);
        assertThat(testClans.getCapitanId()).isEqualTo(DEFAULT_CAPITAN_ID);
    }

    @Test
    @Transactional
    void createClansWithExistingId() throws Exception {
        // Create the Clans with an existing ID
        clans.setId(1L);
        ClansDTO clansDTO = clansMapper.toDto(clans);

        int databaseSizeBeforeCreate = clansRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClansMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClanNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clansRepository.findAll().size();
        // set the field null
        clans.setClanName(null);

        // Create the Clans, which fails.
        ClansDTO clansDTO = clansMapper.toDto(clans);

        restClansMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapitanIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = clansRepository.findAll().size();
        // set the field null
        clans.setCapitanId(null);

        // Create the Clans, which fails.
        ClansDTO clansDTO = clansMapper.toDto(clans);

        restClansMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClans() throws Exception {
        // Initialize the database
        clansRepository.saveAndFlush(clans);

        // Get all the clansList
        restClansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clans.getId().intValue())))
            .andExpect(jsonPath("$.[*].clanName").value(hasItem(DEFAULT_CLAN_NAME)))
            .andExpect(jsonPath("$.[*].capitanId").value(hasItem(DEFAULT_CAPITAN_ID.intValue())));
    }

    @Test
    @Transactional
    void getClans() throws Exception {
        // Initialize the database
        clansRepository.saveAndFlush(clans);

        // Get the clans
        restClansMockMvc
            .perform(get(ENTITY_API_URL_ID, clans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clans.getId().intValue()))
            .andExpect(jsonPath("$.clanName").value(DEFAULT_CLAN_NAME))
            .andExpect(jsonPath("$.capitanId").value(DEFAULT_CAPITAN_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingClans() throws Exception {
        // Get the clans
        restClansMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClans() throws Exception {
        // Initialize the database
        clansRepository.saveAndFlush(clans);

        int databaseSizeBeforeUpdate = clansRepository.findAll().size();

        // Update the clans
        Clans updatedClans = clansRepository.findById(clans.getId()).get();
        // Disconnect from session so that the updates on updatedClans are not directly saved in db
        em.detach(updatedClans);
        updatedClans.clanName(UPDATED_CLAN_NAME).capitanId(UPDATED_CAPITAN_ID);
        ClansDTO clansDTO = clansMapper.toDto(updatedClans);

        restClansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clansDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isOk());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
        Clans testClans = clansList.get(clansList.size() - 1);
        assertThat(testClans.getClanName()).isEqualTo(UPDATED_CLAN_NAME);
        assertThat(testClans.getCapitanId()).isEqualTo(UPDATED_CAPITAN_ID);
    }

    @Test
    @Transactional
    void putNonExistingClans() throws Exception {
        int databaseSizeBeforeUpdate = clansRepository.findAll().size();
        clans.setId(count.incrementAndGet());

        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clansDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClans() throws Exception {
        int databaseSizeBeforeUpdate = clansRepository.findAll().size();
        clans.setId(count.incrementAndGet());

        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClans() throws Exception {
        int databaseSizeBeforeUpdate = clansRepository.findAll().size();
        clans.setId(count.incrementAndGet());

        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClansMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClansWithPatch() throws Exception {
        // Initialize the database
        clansRepository.saveAndFlush(clans);

        int databaseSizeBeforeUpdate = clansRepository.findAll().size();

        // Update the clans using partial update
        Clans partialUpdatedClans = new Clans();
        partialUpdatedClans.setId(clans.getId());

        partialUpdatedClans.clanName(UPDATED_CLAN_NAME).capitanId(UPDATED_CAPITAN_ID);

        restClansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClans.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClans))
            )
            .andExpect(status().isOk());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
        Clans testClans = clansList.get(clansList.size() - 1);
        assertThat(testClans.getClanName()).isEqualTo(UPDATED_CLAN_NAME);
        assertThat(testClans.getCapitanId()).isEqualTo(UPDATED_CAPITAN_ID);
    }

    @Test
    @Transactional
    void fullUpdateClansWithPatch() throws Exception {
        // Initialize the database
        clansRepository.saveAndFlush(clans);

        int databaseSizeBeforeUpdate = clansRepository.findAll().size();

        // Update the clans using partial update
        Clans partialUpdatedClans = new Clans();
        partialUpdatedClans.setId(clans.getId());

        partialUpdatedClans.clanName(UPDATED_CLAN_NAME).capitanId(UPDATED_CAPITAN_ID);

        restClansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClans.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClans))
            )
            .andExpect(status().isOk());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
        Clans testClans = clansList.get(clansList.size() - 1);
        assertThat(testClans.getClanName()).isEqualTo(UPDATED_CLAN_NAME);
        assertThat(testClans.getCapitanId()).isEqualTo(UPDATED_CAPITAN_ID);
    }

    @Test
    @Transactional
    void patchNonExistingClans() throws Exception {
        int databaseSizeBeforeUpdate = clansRepository.findAll().size();
        clans.setId(count.incrementAndGet());

        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clansDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClans() throws Exception {
        int databaseSizeBeforeUpdate = clansRepository.findAll().size();
        clans.setId(count.incrementAndGet());

        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClans() throws Exception {
        int databaseSizeBeforeUpdate = clansRepository.findAll().size();
        clans.setId(count.incrementAndGet());

        // Create the Clans
        ClansDTO clansDTO = clansMapper.toDto(clans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClansMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clansDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clans in the database
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClans() throws Exception {
        // Initialize the database
        clansRepository.saveAndFlush(clans);

        int databaseSizeBeforeDelete = clansRepository.findAll().size();

        // Delete the clans
        restClansMockMvc
            .perform(delete(ENTITY_API_URL_ID, clans.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clans> clansList = clansRepository.findAll();
        assertThat(clansList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
