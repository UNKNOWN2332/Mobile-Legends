package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Turnirs} entity.
 */
public class TurnirsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    private String turnirName;

    private Long createrId;

    private Instant startDate;

    private Instant endDate;

    private Long price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTurnirName() {
        return turnirName;
    }

    public void setTurnirName(String turnirName) {
        this.turnirName = turnirName;
    }

    public Long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Long createrId) {
        this.createrId = createrId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurnirsDTO)) {
            return false;
        }

        TurnirsDTO turnirsDTO = (TurnirsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, turnirsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnirsDTO{" +
            "id=" + getId() +
            ", turnirName='" + getTurnirName() + "'" +
            ", createrId=" + getCreaterId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
