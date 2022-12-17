package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PayDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Pay}.
 */
public interface PayService {
    /**
     * Save a pay.
     *
     * @param payDTO the entity to save.
     * @return the persisted entity.
     */
    PayDTO save(PayDTO payDTO);

    /**
     * Updates a pay.
     *
     * @param payDTO the entity to update.
     * @return the persisted entity.
     */
    PayDTO update(PayDTO payDTO);

    /**
     * Partially updates a pay.
     *
     * @param payDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PayDTO> partialUpdate(PayDTO payDTO);

    /**
     * Get all the pays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pay.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayDTO> findOne(Long id);

    /**
     * Delete the "id" pay.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
