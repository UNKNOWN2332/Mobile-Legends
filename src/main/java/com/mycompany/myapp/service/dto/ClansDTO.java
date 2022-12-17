package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Clans} entity.
 */
public class ClansDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    private String clanName;

    @NotNull
    private Long capitanId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public Long getCapitanId() {
        return capitanId;
    }

    public void setCapitanId(Long capitanId) {
        this.capitanId = capitanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClansDTO)) {
            return false;
        }

        ClansDTO clansDTO = (ClansDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clansDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClansDTO{" +
            "id=" + getId() +
            ", clanName='" + getClanName() + "'" +
            ", capitanId=" + getCapitanId() +
            "}";
    }
}
