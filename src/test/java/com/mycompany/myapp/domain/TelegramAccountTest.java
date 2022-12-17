package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TelegramAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramAccount.class);
        TelegramAccount telegramAccount1 = new TelegramAccount();
        telegramAccount1.setId(1L);
        TelegramAccount telegramAccount2 = new TelegramAccount();
        telegramAccount2.setId(telegramAccount1.getId());
        assertThat(telegramAccount1).isEqualTo(telegramAccount2);
        telegramAccount2.setId(2L);
        assertThat(telegramAccount1).isNotEqualTo(telegramAccount2);
        telegramAccount1.setId(null);
        assertThat(telegramAccount1).isNotEqualTo(telegramAccount2);
    }
}
