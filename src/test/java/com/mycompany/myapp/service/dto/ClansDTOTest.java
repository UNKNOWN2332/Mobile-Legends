package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClansDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClansDTO.class);
        ClansDTO clansDTO1 = new ClansDTO();
        clansDTO1.setId(1L);
        ClansDTO clansDTO2 = new ClansDTO();
        assertThat(clansDTO1).isNotEqualTo(clansDTO2);
        clansDTO2.setId(clansDTO1.getId());
        assertThat(clansDTO1).isEqualTo(clansDTO2);
        clansDTO2.setId(2L);
        assertThat(clansDTO1).isNotEqualTo(clansDTO2);
        clansDTO1.setId(null);
        assertThat(clansDTO1).isNotEqualTo(clansDTO2);
    }
}
