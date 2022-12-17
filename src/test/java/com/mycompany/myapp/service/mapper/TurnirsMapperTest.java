package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnirsMapperTest {

    private TurnirsMapper turnirsMapper;

    @BeforeEach
    public void setUp() {
        turnirsMapper = new TurnirsMapperImpl();
    }
}
