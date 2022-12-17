package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClansTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clans.class);
        Clans clans1 = new Clans();
        clans1.setId(1L);
        Clans clans2 = new Clans();
        clans2.setId(clans1.getId());
        assertThat(clans1).isEqualTo(clans2);
        clans2.setId(2L);
        assertThat(clans1).isNotEqualTo(clans2);
        clans1.setId(null);
        assertThat(clans1).isNotEqualTo(clans2);
    }
}
