package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayDTO.class);
        PayDTO payDTO1 = new PayDTO();
        payDTO1.setId(1L);
        PayDTO payDTO2 = new PayDTO();
        assertThat(payDTO1).isNotEqualTo(payDTO2);
        payDTO2.setId(payDTO1.getId());
        assertThat(payDTO1).isEqualTo(payDTO2);
        payDTO2.setId(2L);
        assertThat(payDTO1).isNotEqualTo(payDTO2);
        payDTO1.setId(null);
        assertThat(payDTO1).isNotEqualTo(payDTO2);
    }
}
