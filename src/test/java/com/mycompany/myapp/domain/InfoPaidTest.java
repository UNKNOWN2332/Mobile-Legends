package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfoPaidTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoPaid.class);
        InfoPaid infoPaid1 = new InfoPaid();
        infoPaid1.setId(1L);
        InfoPaid infoPaid2 = new InfoPaid();
        infoPaid2.setId(infoPaid1.getId());
        assertThat(infoPaid1).isEqualTo(infoPaid2);
        infoPaid2.setId(2L);
        assertThat(infoPaid1).isNotEqualTo(infoPaid2);
        infoPaid1.setId(null);
        assertThat(infoPaid1).isNotEqualTo(infoPaid2);
    }
}
