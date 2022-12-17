package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.DatesOfPeriod;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Period} entity.
 */
public class PeriodDTO implements Serializable {

    private Long id;

    @Size(min = 3, max = 25)
    private String periodName;

    private Instant createAt;

    @DecimalMin(value = "0")
    private Double amount;

    private DatesOfPeriod datesOfPeriod;

    private InfoPaidDTO infoPaid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public DatesOfPeriod getDatesOfPeriod() {
        return datesOfPeriod;
    }

    public void setDatesOfPeriod(DatesOfPeriod datesOfPeriod) {
        this.datesOfPeriod = datesOfPeriod;
    }

    public InfoPaidDTO getInfoPaid() {
        return infoPaid;
    }

    public void setInfoPaid(InfoPaidDTO infoPaid) {
        this.infoPaid = infoPaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodDTO)) {
            return false;
        }

        PeriodDTO periodDTO = (PeriodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, periodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeriodDTO{" +
            "id=" + getId() +
            ", periodName='" + getPeriodName() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", amount=" + getAmount() +
            ", datesOfPeriod='" + getDatesOfPeriod() + "'" +
            ", infoPaid=" + getInfoPaid() +
            "}";
    }
}
