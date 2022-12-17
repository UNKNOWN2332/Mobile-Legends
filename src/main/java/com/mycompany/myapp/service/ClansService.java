package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ClansDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Clans}.
 */
public interface ClansService {
    /**
     * Save a clans.
     *
     * @param clansDTO the entity to save.
     * @return the persisted entity.
     */
    ClansDTO save(ClansDTO clansDTO);

    /**
     * Updates a clans.
     *
     * @param clansDTO the entity to update.
     * @return the persisted entity.
     */
    ClansDTO update(ClansDTO clansDTO);

    /**
     * Partially updates a clans.
     *
     * @param clansDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClansDTO> partialUpdate(ClansDTO clansDTO);

    /**
     * Get all the clans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClansDTO> findAll(Pageable pageable);

    /**
     * Get the "id" clans.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClansDTO> findOne(Long id);

    /**
     * Delete the "id" clans.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
