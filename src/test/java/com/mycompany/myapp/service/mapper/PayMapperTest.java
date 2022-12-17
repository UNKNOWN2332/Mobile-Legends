package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayMapperTest {

    private PayMapper payMapper;

    @BeforeEach
    public void setUp() {
        payMapper = new PayMapperImpl();
    }
}
