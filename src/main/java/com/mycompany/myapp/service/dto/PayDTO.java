package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Pay} entity.
 */
public class PayDTO implements Serializable {

    private Long id;

    @DecimalMin(value = "0")
    private Double amount;

    private Boolean isPaid;

    private Instant paidAt;

    private Instant expiryDate;

    private TelegramAccountDTO account;

    private PeriodDTO period;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TelegramAccountDTO getAccount() {
        return account;
    }

    public void setAccount(TelegramAccountDTO account) {
        this.account = account;
    }

    public PeriodDTO getPeriod() {
        return period;
    }

    public void setPeriod(PeriodDTO period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayDTO)) {
            return false;
        }

        PayDTO payDTO = (PayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, payDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", isPaid='" + getIsPaid() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", account=" + getAccount() +
            ", period=" + getPeriod() +
            "}";
    }
}
