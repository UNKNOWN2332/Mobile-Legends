package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TelegramAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelegramAccountDTO.class);
        TelegramAccountDTO telegramAccountDTO1 = new TelegramAccountDTO();
        telegramAccountDTO1.setId(1L);
        TelegramAccountDTO telegramAccountDTO2 = new TelegramAccountDTO();
        assertThat(telegramAccountDTO1).isNotEqualTo(telegramAccountDTO2);
        telegramAccountDTO2.setId(telegramAccountDTO1.getId());
        assertThat(telegramAccountDTO1).isEqualTo(telegramAccountDTO2);
        telegramAccountDTO2.setId(2L);
        assertThat(telegramAccountDTO1).isNotEqualTo(telegramAccountDTO2);
        telegramAccountDTO1.setId(null);
        assertThat(telegramAccountDTO1).isNotEqualTo(telegramAccountDTO2);
    }
}
