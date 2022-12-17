package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Pay;
import com.mycompany.myapp.repository.PayRepository;
import com.mycompany.myapp.service.dto.PayDTO;
import com.mycompany.myapp.service.mapper.PayMapper;
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
 * Integration tests for the {@link PayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PayResourceIT {

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    private static final Instant DEFAULT_PAID_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAID_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/pays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayMockMvc;

    private Pay pay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pay createEntity(EntityManager em) {
        Pay pay = new Pay().amount(DEFAULT_AMOUNT).isPaid(DEFAULT_IS_PAID).paidAt(DEFAULT_PAID_AT).expiryDate(DEFAULT_EXPIRY_DATE);
        return pay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pay createUpdatedEntity(EntityManager em) {
        Pay pay = new Pay().amount(UPDATED_AMOUNT).isPaid(UPDATED_IS_PAID).paidAt(UPDATED_PAID_AT).expiryDate(UPDATED_EXPIRY_DATE);
        return pay;
    }

    @BeforeEach
    public void initTest() {
        pay = createEntity(em);
    }

    @Test
    @Transactional
    void createPay() throws Exception {
        int databaseSizeBeforeCreate = payRepository.findAll().size();
        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);
        restPayMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeCreate + 1);
        Pay testPay = payList.get(payList.size() - 1);
        assertThat(testPay.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPay.getIsPaid()).isEqualTo(DEFAULT_IS_PAID);
        assertThat(testPay.getPaidAt()).isEqualTo(DEFAULT_PAID_AT);
        assertThat(testPay.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void createPayWithExistingId() throws Exception {
        // Create the Pay with an existing ID
        pay.setId(1L);
        PayDTO payDTO = payMapper.toDto(pay);

        int databaseSizeBeforeCreate = payRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPays() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        // Get all the payList
        restPayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pay.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].paidAt").value(hasItem(DEFAULT_PAID_AT.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }

    @Test
    @Transactional
    void getPay() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        // Get the pay
        restPayMockMvc
            .perform(get(ENTITY_API_URL_ID, pay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pay.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()))
            .andExpect(jsonPath("$.paidAt").value(DEFAULT_PAID_AT.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPay() throws Exception {
        // Get the pay
        restPayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPay() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        int databaseSizeBeforeUpdate = payRepository.findAll().size();

        // Update the pay
        Pay updatedPay = payRepository.findById(pay.getId()).get();
        // Disconnect from session so that the updates on updatedPay are not directly saved in db
        em.detach(updatedPay);
        updatedPay.amount(UPDATED_AMOUNT).isPaid(UPDATED_IS_PAID).paidAt(UPDATED_PAID_AT).expiryDate(UPDATED_EXPIRY_DATE);
        PayDTO payDTO = payMapper.toDto(updatedPay);

        restPayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
        Pay testPay = payList.get(payList.size() - 1);
        assertThat(testPay.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPay.getIsPaid()).isEqualTo(UPDATED_IS_PAID);
        assertThat(testPay.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
        assertThat(testPay.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();
        pay.setId(count.incrementAndGet());

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();
        pay.setId(count.incrementAndGet());

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();
        pay.setId(count.incrementAndGet());

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayWithPatch() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        int databaseSizeBeforeUpdate = payRepository.findAll().size();

        // Update the pay using partial update
        Pay partialUpdatedPay = new Pay();
        partialUpdatedPay.setId(pay.getId());

        partialUpdatedPay.amount(UPDATED_AMOUNT).paidAt(UPDATED_PAID_AT);

        restPayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPay.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPay))
            )
            .andExpect(status().isOk());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
        Pay testPay = payList.get(payList.size() - 1);
        assertThat(testPay.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPay.getIsPaid()).isEqualTo(DEFAULT_IS_PAID);
        assertThat(testPay.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
        assertThat(testPay.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePayWithPatch() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        int databaseSizeBeforeUpdate = payRepository.findAll().size();

        // Update the pay using partial update
        Pay partialUpdatedPay = new Pay();
        partialUpdatedPay.setId(pay.getId());

        partialUpdatedPay.amount(UPDATED_AMOUNT).isPaid(UPDATED_IS_PAID).paidAt(UPDATED_PAID_AT).expiryDate(UPDATED_EXPIRY_DATE);

        restPayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPay.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPay))
            )
            .andExpect(status().isOk());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
        Pay testPay = payList.get(payList.size() - 1);
        assertThat(testPay.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPay.getIsPaid()).isEqualTo(UPDATED_IS_PAID);
        assertThat(testPay.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
        assertThat(testPay.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();
        pay.setId(count.incrementAndGet());

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();
        pay.setId(count.incrementAndGet());

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();
        pay.setId(count.incrementAndGet());

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePay() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        int databaseSizeBeforeDelete = payRepository.findAll().size();

        // Delete the pay
        restPayMockMvc
            .perform(delete(ENTITY_API_URL_ID, pay.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
