package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Clans;
import com.mycompany.myapp.repository.ClansRepository;
import com.mycompany.myapp.service.ClansService;
import com.mycompany.myapp.service.dto.ClansDTO;
import com.mycompany.myapp.service.mapper.ClansMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Clans}.
 */
@Service
@Transactional
public class ClansServiceImpl implements ClansService {

    private final Logger log = LoggerFactory.getLogger(ClansServiceImpl.class);

    private final ClansRepository clansRepository;

    private final ClansMapper clansMapper;

    public ClansServiceImpl(ClansRepository clansRepository, ClansMapper clansMapper) {
        this.clansRepository = clansRepository;
        this.clansMapper = clansMapper;
    }

    @Override
    public ClansDTO save(ClansDTO clansDTO) {
        log.debug("Request to save Clans : {}", clansDTO);
        Clans clans = clansMapper.toEntity(clansDTO);
        clans = clansRepository.save(clans);
        return clansMapper.toDto(clans);
    }

    @Override
    public ClansDTO update(ClansDTO clansDTO) {
        log.debug("Request to save Clans : {}", clansDTO);
        Clans clans = clansMapper.toEntity(clansDTO);
        clans = clansRepository.save(clans);
        return clansMapper.toDto(clans);
    }

    @Override
    public Optional<ClansDTO> partialUpdate(ClansDTO clansDTO) {
        log.debug("Request to partially update Clans : {}", clansDTO);

        return clansRepository
            .findById(clansDTO.getId())
            .map(existingClans -> {
                clansMapper.partialUpdate(existingClans, clansDTO);

                return existingClans;
            })
            .map(clansRepository::save)
            .map(clansMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClansDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clans");
        return clansRepository.findAll(pageable).map(clansMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClansDTO> findOne(Long id) {
        log.debug("Request to get Clans : {}", id);
        return clansRepository.findById(id).map(clansMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Clans : {}", id);
        clansRepository.deleteById(id);
    }
}
