package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InfoPaidMapperTest {

    private InfoPaidMapper infoPaidMapper;

    @BeforeEach
    public void setUp() {
        infoPaidMapper = new InfoPaidMapperImpl();
    }
}
