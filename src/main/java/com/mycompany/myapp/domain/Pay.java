package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pay.
 */
@Entity
@Table(name = "pay")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "amount")
    private Double amount;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pays", "infoPaid" }, allowSetters = true)
    private TelegramAccount account;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pays", "infoPaid" }, allowSetters = true)
    private Period period;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pay id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Pay amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getIsPaid() {
        return this.isPaid;
    }

    public Pay isPaid(Boolean isPaid) {
        this.setIsPaid(isPaid);
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Instant getPaidAt() {
        return this.paidAt;
    }

    public Pay paidAt(Instant paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public Pay expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TelegramAccount getAccount() {
        return this.account;
    }

    public void setAccount(TelegramAccount telegramAccount) {
        this.account = telegramAccount;
    }

    public Pay account(TelegramAccount telegramAccount) {
        this.setAccount(telegramAccount);
        return this;
    }

    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Pay period(Period period) {
        this.setPeriod(period);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pay)) {
            return false;
        }
        return id != null && id.equals(((Pay) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pay{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", isPaid='" + getIsPaid() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            "}";
    }
}
