package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnirsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Turnirs.class);
        Turnirs turnirs1 = new Turnirs();
        turnirs1.setId(1L);
        Turnirs turnirs2 = new Turnirs();
        turnirs2.setId(turnirs1.getId());
        assertThat(turnirs1).isEqualTo(turnirs2);
        turnirs2.setId(2L);
        assertThat(turnirs1).isNotEqualTo(turnirs2);
        turnirs1.setId(null);
        assertThat(turnirs1).isNotEqualTo(turnirs2);
    }
}
