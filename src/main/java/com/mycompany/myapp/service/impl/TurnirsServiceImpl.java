package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Turnirs;
import com.mycompany.myapp.repository.TurnirsRepository;
import com.mycompany.myapp.service.TurnirsService;
import com.mycompany.myapp.service.dto.TurnirsDTO;
import com.mycompany.myapp.service.mapper.TurnirsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Turnirs}.
 */
@Service
@Transactional
public class TurnirsServiceImpl implements TurnirsService {

    private final Logger log = LoggerFactory.getLogger(TurnirsServiceImpl.class);

    private final TurnirsRepository turnirsRepository;

    private final TurnirsMapper turnirsMapper;

    public TurnirsServiceImpl(TurnirsRepository turnirsRepository, TurnirsMapper turnirsMapper) {
        this.turnirsRepository = turnirsRepository;
        this.turnirsMapper = turnirsMapper;
    }

    @Override
    public TurnirsDTO save(TurnirsDTO turnirsDTO) {
        log.debug("Request to save Turnirs : {}", turnirsDTO);
        Turnirs turnirs = turnirsMapper.toEntity(turnirsDTO);
        turnirs = turnirsRepository.save(turnirs);
        return turnirsMapper.toDto(turnirs);
    }

    @Override
    public TurnirsDTO update(TurnirsDTO turnirsDTO) {
        log.debug("Request to save Turnirs : {}", turnirsDTO);
        Turnirs turnirs = turnirsMapper.toEntity(turnirsDTO);
        turnirs = turnirsRepository.save(turnirs);
        return turnirsMapper.toDto(turnirs);
    }

    @Override
    public Optional<TurnirsDTO> partialUpdate(TurnirsDTO turnirsDTO) {
        log.debug("Request to partially update Turnirs : {}", turnirsDTO);

        return turnirsRepository
            .findById(turnirsDTO.getId())
            .map(existingTurnirs -> {
                turnirsMapper.partialUpdate(existingTurnirs, turnirsDTO);

                return existingTurnirs;
            })
            .map(turnirsRepository::save)
            .map(turnirsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TurnirsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Turnirs");
        return turnirsRepository.findAll(pageable).map(turnirsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TurnirsDTO> findOne(Long id) {
        log.debug("Request to get Turnirs : {}", id);
        return turnirsRepository.findById(id).map(turnirsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Turnirs : {}", id);
        turnirsRepository.deleteById(id);
    }
}
