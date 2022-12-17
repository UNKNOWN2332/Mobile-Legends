package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeriodMapperTest {

    private PeriodMapper periodMapper;

    @BeforeEach
    public void setUp() {
        periodMapper = new PeriodMapperImpl();
    }
}
