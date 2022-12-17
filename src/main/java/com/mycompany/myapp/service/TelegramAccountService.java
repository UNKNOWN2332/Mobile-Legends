package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TelegramAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.TelegramAccount}.
 */
public interface TelegramAccountService {
    /**
     * Save a telegramAccount.
     *
     * @param telegramAccountDTO the entity to save.
     * @return the persisted entity.
     */
    TelegramAccountDTO save(TelegramAccountDTO telegramAccountDTO);

    /**
     * Updates a telegramAccount.
     *
     * @param telegramAccountDTO the entity to update.
     * @return the persisted entity.
     */
    TelegramAccountDTO update(TelegramAccountDTO telegramAccountDTO);

    /**
     * Partially updates a telegramAccount.
     *
     * @param telegramAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TelegramAccountDTO> partialUpdate(TelegramAccountDTO telegramAccountDTO);

    /**
     * Get all the telegramAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TelegramAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" telegramAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TelegramAccountDTO> findOne(Long id);

    /**
     * Delete the "id" telegramAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
