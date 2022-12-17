package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClansMapperTest {

    private ClansMapper clansMapper;

    @BeforeEach
    public void setUp() {
        clansMapper = new ClansMapperImpl();
    }
}
