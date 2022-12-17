package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfoPaidDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoPaidDTO.class);
        InfoPaidDTO infoPaidDTO1 = new InfoPaidDTO();
        infoPaidDTO1.setId(1L);
        InfoPaidDTO infoPaidDTO2 = new InfoPaidDTO();
        assertThat(infoPaidDTO1).isNotEqualTo(infoPaidDTO2);
        infoPaidDTO2.setId(infoPaidDTO1.getId());
        assertThat(infoPaidDTO1).isEqualTo(infoPaidDTO2);
        infoPaidDTO2.setId(2L);
        assertThat(infoPaidDTO1).isNotEqualTo(infoPaidDTO2);
        infoPaidDTO1.setId(null);
        assertThat(infoPaidDTO1).isNotEqualTo(infoPaidDTO2);
    }
}
