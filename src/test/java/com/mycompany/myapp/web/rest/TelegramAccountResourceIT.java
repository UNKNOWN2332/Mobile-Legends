package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.domain.enumeration.Role;
import com.mycompany.myapp.domain.enumeration.Types;
import com.mycompany.myapp.repository.TelegramAccountRepository;
import com.mycompany.myapp.service.dto.TelegramAccountDTO;
import com.mycompany.myapp.service.mapper.TelegramAccountMapper;
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
 * Integration tests for the {@link TelegramAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TelegramAccountResourceIT {

    private static final Long DEFAULT_CHAT_ID = 1L;
    private static final Long UPDATED_CHAT_ID = 2L;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.Capitan;
    private static final Role UPDATED_ROLE = Role.Member;

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MOBILE_LEGEND_ID = 1L;
    private static final Long UPDATED_MOBILE_LEGEND_ID = 2L;

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

    private static final Types DEFAULT_TYPES = Types.FIRSTNAME;
    private static final Types UPDATED_TYPES = Types.LASTNAME;

    private static final String ENTITY_API_URL = "/api/telegram-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TelegramAccountRepository telegramAccountRepository;

    @Autowired
    private TelegramAccountMapper telegramAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTelegramAccountMockMvc;

    private TelegramAccount telegramAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramAccount createEntity(EntityManager em) {
        TelegramAccount telegramAccount = new TelegramAccount()
            .chatId(DEFAULT_CHAT_ID)
            .username(DEFAULT_USERNAME)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .role(DEFAULT_ROLE)
            .createAt(DEFAULT_CREATE_AT)
            .mobileLegendId(DEFAULT_MOBILE_LEGEND_ID)
            .nickName(DEFAULT_NICK_NAME)
            .types(DEFAULT_TYPES);
        return telegramAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelegramAccount createUpdatedEntity(EntityManager em) {
        TelegramAccount telegramAccount = new TelegramAccount()
            .chatId(UPDATED_CHAT_ID)
            .username(UPDATED_USERNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .role(UPDATED_ROLE)
            .createAt(UPDATED_CREATE_AT)
            .mobileLegendId(UPDATED_MOBILE_LEGEND_ID)
            .nickName(UPDATED_NICK_NAME)
            .types(UPDATED_TYPES);
        return telegramAccount;
    }

    @BeforeEach
    public void initTest() {
        telegramAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createTelegramAccount() throws Exception {
        int databaseSizeBeforeCreate = telegramAccountRepository.findAll().size();
        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);
        restTelegramAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeCreate + 1);
        TelegramAccount testTelegramAccount = telegramAccountList.get(telegramAccountList.size() - 1);
        assertThat(testTelegramAccount.getChatId()).isEqualTo(DEFAULT_CHAT_ID);
        assertThat(testTelegramAccount.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTelegramAccount.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testTelegramAccount.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testTelegramAccount.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTelegramAccount.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testTelegramAccount.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testTelegramAccount.getMobileLegendId()).isEqualTo(DEFAULT_MOBILE_LEGEND_ID);
        assertThat(testTelegramAccount.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testTelegramAccount.getTypes()).isEqualTo(DEFAULT_TYPES);
    }

    @Test
    @Transactional
    void createTelegramAccountWithExistingId() throws Exception {
        // Create the TelegramAccount with an existing ID
        telegramAccount.setId(1L);
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        int databaseSizeBeforeCreate = telegramAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelegramAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTelegramAccounts() throws Exception {
        // Initialize the database
        telegramAccountRepository.saveAndFlush(telegramAccount);

        // Get all the telegramAccountList
        restTelegramAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telegramAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].chatId").value(hasItem(DEFAULT_CHAT_ID.intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].mobileLegendId").value(hasItem(DEFAULT_MOBILE_LEGEND_ID.intValue())))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].types").value(hasItem(DEFAULT_TYPES.toString())));
    }

    @Test
    @Transactional
    void getTelegramAccount() throws Exception {
        // Initialize the database
        telegramAccountRepository.saveAndFlush(telegramAccount);

        // Get the telegramAccount
        restTelegramAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, telegramAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(telegramAccount.getId().intValue()))
            .andExpect(jsonPath("$.chatId").value(DEFAULT_CHAT_ID.intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.mobileLegendId").value(DEFAULT_MOBILE_LEGEND_ID.intValue()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.types").value(DEFAULT_TYPES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTelegramAccount() throws Exception {
        // Get the telegramAccount
        restTelegramAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTelegramAccount() throws Exception {
        // Initialize the database
        telegramAccountRepository.saveAndFlush(telegramAccount);

        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();

        // Update the telegramAccount
        TelegramAccount updatedTelegramAccount = telegramAccountRepository.findById(telegramAccount.getId()).get();
        // Disconnect from session so that the updates on updatedTelegramAccount are not directly saved in db
        em.detach(updatedTelegramAccount);
        updatedTelegramAccount
            .chatId(UPDATED_CHAT_ID)
            .username(UPDATED_USERNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .role(UPDATED_ROLE)
            .createAt(UPDATED_CREATE_AT)
            .mobileLegendId(UPDATED_MOBILE_LEGEND_ID)
            .nickName(UPDATED_NICK_NAME)
            .types(UPDATED_TYPES);
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(updatedTelegramAccount);

        restTelegramAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, telegramAccountDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
        TelegramAccount testTelegramAccount = telegramAccountList.get(telegramAccountList.size() - 1);
        assertThat(testTelegramAccount.getChatId()).isEqualTo(UPDATED_CHAT_ID);
        assertThat(testTelegramAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTelegramAccount.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTelegramAccount.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testTelegramAccount.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTelegramAccount.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTelegramAccount.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testTelegramAccount.getMobileLegendId()).isEqualTo(UPDATED_MOBILE_LEGEND_ID);
        assertThat(testTelegramAccount.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testTelegramAccount.getTypes()).isEqualTo(UPDATED_TYPES);
    }

    @Test
    @Transactional
    void putNonExistingTelegramAccount() throws Exception {
        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();
        telegramAccount.setId(count.incrementAndGet());

        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelegramAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, telegramAccountDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTelegramAccount() throws Exception {
        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();
        telegramAccount.setId(count.incrementAndGet());

        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTelegramAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTelegramAccount() throws Exception {
        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();
        telegramAccount.setId(count.incrementAndGet());

        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTelegramAccountMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTelegramAccountWithPatch() throws Exception {
        // Initialize the database
        telegramAccountRepository.saveAndFlush(telegramAccount);

        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();

        // Update the telegramAccount using partial update
        TelegramAccount partialUpdatedTelegramAccount = new TelegramAccount();
        partialUpdatedTelegramAccount.setId(telegramAccount.getId());

        partialUpdatedTelegramAccount
            .lastname(UPDATED_LASTNAME)
            .role(UPDATED_ROLE)
            .mobileLegendId(UPDATED_MOBILE_LEGEND_ID)
            .nickName(UPDATED_NICK_NAME)
            .types(UPDATED_TYPES);

        restTelegramAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTelegramAccount.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTelegramAccount))
            )
            .andExpect(status().isOk());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
        TelegramAccount testTelegramAccount = telegramAccountList.get(telegramAccountList.size() - 1);
        assertThat(testTelegramAccount.getChatId()).isEqualTo(DEFAULT_CHAT_ID);
        assertThat(testTelegramAccount.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTelegramAccount.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testTelegramAccount.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testTelegramAccount.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTelegramAccount.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTelegramAccount.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testTelegramAccount.getMobileLegendId()).isEqualTo(UPDATED_MOBILE_LEGEND_ID);
        assertThat(testTelegramAccount.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testTelegramAccount.getTypes()).isEqualTo(UPDATED_TYPES);
    }

    @Test
    @Transactional
    void fullUpdateTelegramAccountWithPatch() throws Exception {
        // Initialize the database
        telegramAccountRepository.saveAndFlush(telegramAccount);

        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();

        // Update the telegramAccount using partial update
        TelegramAccount partialUpdatedTelegramAccount = new TelegramAccount();
        partialUpdatedTelegramAccount.setId(telegramAccount.getId());

        partialUpdatedTelegramAccount
            .chatId(UPDATED_CHAT_ID)
            .username(UPDATED_USERNAME)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .role(UPDATED_ROLE)
            .createAt(UPDATED_CREATE_AT)
            .mobileLegendId(UPDATED_MOBILE_LEGEND_ID)
            .nickName(UPDATED_NICK_NAME)
            .types(UPDATED_TYPES);

        restTelegramAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTelegramAccount.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTelegramAccount))
            )
            .andExpect(status().isOk());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
        TelegramAccount testTelegramAccount = telegramAccountList.get(telegramAccountList.size() - 1);
        assertThat(testTelegramAccount.getChatId()).isEqualTo(UPDATED_CHAT_ID);
        assertThat(testTelegramAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTelegramAccount.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTelegramAccount.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testTelegramAccount.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTelegramAccount.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTelegramAccount.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testTelegramAccount.getMobileLegendId()).isEqualTo(UPDATED_MOBILE_LEGEND_ID);
        assertThat(testTelegramAccount.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testTelegramAccount.getTypes()).isEqualTo(UPDATED_TYPES);
    }

    @Test
    @Transactional
    void patchNonExistingTelegramAccount() throws Exception {
        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();
        telegramAccount.setId(count.incrementAndGet());

        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelegramAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, telegramAccountDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTelegramAccount() throws Exception {
        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();
        telegramAccount.setId(count.incrementAndGet());

        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTelegramAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTelegramAccount() throws Exception {
        int databaseSizeBeforeUpdate = telegramAccountRepository.findAll().size();
        telegramAccount.setId(count.incrementAndGet());

        // Create the TelegramAccount
        TelegramAccountDTO telegramAccountDTO = telegramAccountMapper.toDto(telegramAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTelegramAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(telegramAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TelegramAccount in the database
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTelegramAccount() throws Exception {
        // Initialize the database
        telegramAccountRepository.saveAndFlush(telegramAccount);

        int databaseSizeBeforeDelete = telegramAccountRepository.findAll().size();

        // Delete the telegramAccount
        restTelegramAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, telegramAccount.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TelegramAccount> telegramAccountList = telegramAccountRepository.findAll();
        assertThat(telegramAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
