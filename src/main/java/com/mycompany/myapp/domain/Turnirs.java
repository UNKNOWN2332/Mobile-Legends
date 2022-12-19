package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Turnirs.
 */
@Entity
@Table(name = "turnirs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Turnirs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "turnir_name", length = 15, nullable = false)
    private String turnirName;

    @Column(name = "creater_id")
    private Long createrId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "price")
    private Long price;

    @OneToMany(mappedBy = "turnirs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "turnirs" }, allowSetters = true)
    private Set<Clans> clans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Turnirs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTurnirName() {
        return this.turnirName;
    }

    public Turnirs turnirName(String turnirName) {
        this.setTurnirName(turnirName);
        return this;
    }

    public void setTurnirName(String turnirName) {
        this.turnirName = turnirName;
    }

    public Long getCreaterId() {
        return this.createrId;
    }

    public Turnirs createrId(Long createrId) {
        this.setCreaterId(createrId);
        return this;
    }

    public void setCreaterId(Long createrId) {
        this.createrId = createrId;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Turnirs startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Turnirs endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getPrice() {
        return this.price;
    }

    public Turnirs price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Set<Clans> getClans() {
        return this.clans;
    }

    public void setClans(Set<Clans> clans) {
        if (this.clans != null) {
            this.clans.forEach(i -> i.setTurnirs(null));
        }
        if (clans != null) {
            clans.forEach(i -> i.setTurnirs(this));
        }
        this.clans = clans;
    }

    public Turnirs clans(Set<Clans> clans) {
        this.setClans(clans);
        return this;
    }

    public Turnirs addClans(Clans clans) {
        this.clans.add(clans);
        clans.setTurnirs(this);
        return this;
    }

    public Turnirs removeClans(Clans clans) {
        this.clans.remove(clans);
        clans.setTurnirs(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turnirs)) {
            return false;
        }
        return id != null && id.equals(((Turnirs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Turnirs{" +
            "id=" + getId() +
            ", turnirName='" + getTurnirName() + "'" +
            ", createrId=" + getCreaterId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
