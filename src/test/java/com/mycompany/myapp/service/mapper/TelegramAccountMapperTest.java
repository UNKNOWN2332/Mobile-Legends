package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TelegramAccountMapperTest {

    private TelegramAccountMapper telegramAccountMapper;

    @BeforeEach
    public void setUp() {
        telegramAccountMapper = new TelegramAccountMapperImpl();
    }
}
