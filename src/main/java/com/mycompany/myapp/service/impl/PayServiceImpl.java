package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Pay;
import com.mycompany.myapp.repository.PayRepository;
import com.mycompany.myapp.service.PayService;
import com.mycompany.myapp.service.dto.PayDTO;
import com.mycompany.myapp.service.mapper.PayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pay}.
 */
@Service
@Transactional
public class PayServiceImpl implements PayService {

    private final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);

    private final PayRepository payRepository;

    private final PayMapper payMapper;

    public PayServiceImpl(PayRepository payRepository, PayMapper payMapper) {
        this.payRepository = payRepository;
        this.payMapper = payMapper;
    }

    @Override
    public PayDTO save(PayDTO payDTO) {
        log.debug("Request to save Pay : {}", payDTO);
        Pay pay = payMapper.toEntity(payDTO);
        pay = payRepository.save(pay);
        return payMapper.toDto(pay);
    }

    @Override
    public PayDTO update(PayDTO payDTO) {
        log.debug("Request to save Pay : {}", payDTO);
        Pay pay = payMapper.toEntity(payDTO);
        pay = payRepository.save(pay);
        return payMapper.toDto(pay);
    }

    @Override
    public Optional<PayDTO> partialUpdate(PayDTO payDTO) {
        log.debug("Request to partially update Pay : {}", payDTO);

        return payRepository
            .findById(payDTO.getId())
            .map(existingPay -> {
                payMapper.partialUpdate(existingPay, payDTO);

                return existingPay;
            })
            .map(payRepository::save)
            .map(payMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pays");
        return payRepository.findAll(pageable).map(payMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PayDTO> findOne(Long id) {
        log.debug("Request to get Pay : {}", id);
        return payRepository.findById(id).map(payMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pay : {}", id);
        payRepository.deleteById(id);
    }
}
