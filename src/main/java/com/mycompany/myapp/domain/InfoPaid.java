package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InfoPaid.
 */
@Entity
@Table(name = "info_paid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InfoPaid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @OneToMany(mappedBy = "infoPaid")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "infoPaid" }, allowSetters = true)
    private Set<TelegramAccount> accounts = new HashSet<>();

    @OneToMany(mappedBy = "infoPaid")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "infoPaid" }, allowSetters = true)
    private Set<Period> periods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfoPaid id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public InfoPaid expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Set<TelegramAccount> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Set<TelegramAccount> telegramAccounts) {
        if (this.accounts != null) {
            this.accounts.forEach(i -> i.setInfoPaid(null));
        }
        if (telegramAccounts != null) {
            telegramAccounts.forEach(i -> i.setInfoPaid(this));
        }
        this.accounts = telegramAccounts;
    }

    public InfoPaid accounts(Set<TelegramAccount> telegramAccounts) {
        this.setAccounts(telegramAccounts);
        return this;
    }

    public InfoPaid addAccount(TelegramAccount telegramAccount) {
        this.accounts.add(telegramAccount);
        telegramAccount.setInfoPaid(this);
        return this;
    }

    public InfoPaid removeAccount(TelegramAccount telegramAccount) {
        this.accounts.remove(telegramAccount);
        telegramAccount.setInfoPaid(null);
        return this;
    }

    public Set<Period> getPeriods() {
        return this.periods;
    }

    public void setPeriods(Set<Period> periods) {
        if (this.periods != null) {
            this.periods.forEach(i -> i.setInfoPaid(null));
        }
        if (periods != null) {
            periods.forEach(i -> i.setInfoPaid(this));
        }
        this.periods = periods;
    }

    public InfoPaid periods(Set<Period> periods) {
        this.setPeriods(periods);
        return this;
    }

    public InfoPaid addPeriod(Period period) {
        this.periods.add(period);
        period.setInfoPaid(this);
        return this;
    }

    public InfoPaid removePeriod(Period period) {
        this.periods.remove(period);
        period.setInfoPaid(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoPaid)) {
            return false;
        }
        return id != null && id.equals(((InfoPaid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoPaid{" +
            "id=" + getId() +
            ", expiryDate='" + getExpiryDate() + "'" +
            "}";
    }
}
