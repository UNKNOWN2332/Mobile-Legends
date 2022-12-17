package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TurnirsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Turnirs}.
 */
public interface TurnirsService {
    /**
     * Save a turnirs.
     *
     * @param turnirsDTO the entity to save.
     * @return the persisted entity.
     */
    TurnirsDTO save(TurnirsDTO turnirsDTO);

    /**
     * Updates a turnirs.
     *
     * @param turnirsDTO the entity to update.
     * @return the persisted entity.
     */
    TurnirsDTO update(TurnirsDTO turnirsDTO);

    /**
     * Partially updates a turnirs.
     *
     * @param turnirsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TurnirsDTO> partialUpdate(TurnirsDTO turnirsDTO);

    /**
     * Get all the turnirs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TurnirsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" turnirs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TurnirsDTO> findOne(Long id);

    /**
     * Delete the "id" turnirs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
