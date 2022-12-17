package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.TelegramAccount;
import com.mycompany.myapp.repository.TelegramAccountRepository;
import com.mycompany.myapp.service.TelegramAccountService;
import com.mycompany.myapp.service.dto.TelegramAccountDTO;
import com.mycompany.myapp.service.mapper.TelegramAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TelegramAccount}.
 */
@Service
@Transactional
public class TelegramAccountServiceImpl implements TelegramAccountService {

    private final Logger log = LoggerFactory.getLogger(TelegramAccountServiceImpl.class);

    private final TelegramAccountRepository telegramAccountRepository;

    private final TelegramAccountMapper telegramAccountMapper;

    public TelegramAccountServiceImpl(TelegramAccountRepository telegramAccountRepository, TelegramAccountMapper telegramAccountMapper) {
        this.telegramAccountRepository = telegramAccountRepository;
        this.telegramAccountMapper = telegramAccountMapper;
    }

    @Override
    public TelegramAccountDTO save(TelegramAccountDTO telegramAccountDTO) {
        log.debug("Request to save TelegramAccount : {}", telegramAccountDTO);
        TelegramAccount telegramAccount = telegramAccountMapper.toEntity(telegramAccountDTO);
        telegramAccount = telegramAccountRepository.save(telegramAccount);
        return telegramAccountMapper.toDto(telegramAccount);
    }

    @Override
    public TelegramAccountDTO update(TelegramAccountDTO telegramAccountDTO) {
        log.debug("Request to save TelegramAccount : {}", telegramAccountDTO);
        TelegramAccount telegramAccount = telegramAccountMapper.toEntity(telegramAccountDTO);
        telegramAccount = telegramAccountRepository.save(telegramAccount);
        return telegramAccountMapper.toDto(telegramAccount);
    }

    @Override
    public Optional<TelegramAccountDTO> partialUpdate(TelegramAccountDTO telegramAccountDTO) {
        log.debug("Request to partially update TelegramAccount : {}", telegramAccountDTO);

        return telegramAccountRepository
            .findById(telegramAccountDTO.getId())
            .map(existingTelegramAccount -> {
                telegramAccountMapper.partialUpdate(existingTelegramAccount, telegramAccountDTO);

                return existingTelegramAccount;
            })
            .map(telegramAccountRepository::save)
            .map(telegramAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TelegramAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TelegramAccounts");
        return telegramAccountRepository.findAll(pageable).map(telegramAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TelegramAccountDTO> findOne(Long id) {
        log.debug("Request to get TelegramAccount : {}", id);
        return telegramAccountRepository.findById(id).map(telegramAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TelegramAccount : {}", id);
        telegramAccountRepository.deleteById(id);
    }
}
