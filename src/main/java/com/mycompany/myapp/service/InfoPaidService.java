package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.InfoPaidDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.InfoPaid}.
 */
public interface InfoPaidService {
    /**
     * Save a infoPaid.
     *
     * @param infoPaidDTO the entity to save.
     * @return the persisted entity.
     */
    InfoPaidDTO save(InfoPaidDTO infoPaidDTO);

    /**
     * Updates a infoPaid.
     *
     * @param infoPaidDTO the entity to update.
     * @return the persisted entity.
     */
    InfoPaidDTO update(InfoPaidDTO infoPaidDTO);

    /**
     * Partially updates a infoPaid.
     *
     * @param infoPaidDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InfoPaidDTO> partialUpdate(InfoPaidDTO infoPaidDTO);

    /**
     * Get all the infoPaids.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfoPaidDTO> findAll(Pageable pageable);

    /**
     * Get the "id" infoPaid.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InfoPaidDTO> findOne(Long id);

    /**
     * Delete the "id" infoPaid.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
