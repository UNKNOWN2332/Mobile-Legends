package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnirsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnirsDTO.class);
        TurnirsDTO turnirsDTO1 = new TurnirsDTO();
        turnirsDTO1.setId(1L);
        TurnirsDTO turnirsDTO2 = new TurnirsDTO();
        assertThat(turnirsDTO1).isNotEqualTo(turnirsDTO2);
        turnirsDTO2.setId(turnirsDTO1.getId());
        assertThat(turnirsDTO1).isEqualTo(turnirsDTO2);
        turnirsDTO2.setId(2L);
        assertThat(turnirsDTO1).isNotEqualTo(turnirsDTO2);
        turnirsDTO1.setId(null);
        assertThat(turnirsDTO1).isNotEqualTo(turnirsDTO2);
    }
}
