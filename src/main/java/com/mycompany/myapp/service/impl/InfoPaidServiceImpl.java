package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.InfoPaid;
import com.mycompany.myapp.repository.InfoPaidRepository;
import com.mycompany.myapp.service.InfoPaidService;
import com.mycompany.myapp.service.dto.InfoPaidDTO;
import com.mycompany.myapp.service.mapper.InfoPaidMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfoPaid}.
 */
@Service
@Transactional
public class InfoPaidServiceImpl implements InfoPaidService {

    private final Logger log = LoggerFactory.getLogger(InfoPaidServiceImpl.class);

    private final InfoPaidRepository infoPaidRepository;

    private final InfoPaidMapper infoPaidMapper;

    public InfoPaidServiceImpl(InfoPaidRepository infoPaidRepository, InfoPaidMapper infoPaidMapper) {
        this.infoPaidRepository = infoPaidRepository;
        this.infoPaidMapper = infoPaidMapper;
    }

    @Override
    public InfoPaidDTO save(InfoPaidDTO infoPaidDTO) {
        log.debug("Request to save InfoPaid : {}", infoPaidDTO);
        InfoPaid infoPaid = infoPaidMapper.toEntity(infoPaidDTO);
        infoPaid = infoPaidRepository.save(infoPaid);
        return infoPaidMapper.toDto(infoPaid);
    }

    @Override
    public InfoPaidDTO update(InfoPaidDTO infoPaidDTO) {
        log.debug("Request to save InfoPaid : {}", infoPaidDTO);
        InfoPaid infoPaid = infoPaidMapper.toEntity(infoPaidDTO);
        infoPaid = infoPaidRepository.save(infoPaid);
        return infoPaidMapper.toDto(infoPaid);
    }

    @Override
    public Optional<InfoPaidDTO> partialUpdate(InfoPaidDTO infoPaidDTO) {
        log.debug("Request to partially update InfoPaid : {}", infoPaidDTO);

        return infoPaidRepository
            .findById(infoPaidDTO.getId())
            .map(existingInfoPaid -> {
                infoPaidMapper.partialUpdate(existingInfoPaid, infoPaidDTO);

                return existingInfoPaid;
            })
            .map(infoPaidRepository::save)
            .map(infoPaidMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InfoPaidDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InfoPaids");
        return infoPaidRepository.findAll(pageable).map(infoPaidMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InfoPaidDTO> findOne(Long id) {
        log.debug("Request to get InfoPaid : {}", id);
        return infoPaidRepository.findById(id).map(infoPaidMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InfoPaid : {}", id);
        infoPaidRepository.deleteById(id);
    }
}
