package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Role;
import com.mycompany.myapp.domain.enumeration.Types;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TelegramAccount.
 */
@Entity
@Table(name = "telegram_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TelegramAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", unique = true)
    private Long chatId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "create_at")
    private Instant createAt = Instant.now();

    @Column(name = "mobile_legend_id", unique = true)
    private Long mobileLegendId;

    @Column(name = "nick_name")
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "types")
    private Types types = Types.FIRSTNAME;

    @OneToMany(mappedBy = "account")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "account", "period" }, allowSetters = true)
    private Set<Pay> pays = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "accounts", "periods" }, allowSetters = true)
    private InfoPaid infoPaid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TelegramAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return this.chatId;
    }

    public TelegramAccount chatId(Long chatId) {
        this.setChatId(chatId);
        return this;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return this.username;
    }

    public TelegramAccount username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public TelegramAccount firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public TelegramAccount lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public TelegramAccount phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return this.role;
    }

    public TelegramAccount role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public TelegramAccount createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Long getMobileLegendId() {
        return this.mobileLegendId;
    }

    public TelegramAccount mobileLegendId(Long mobileLegendId) {
        this.setMobileLegendId(mobileLegendId);
        return this;
    }

    public void setMobileLegendId(Long mobileLegendId) {
        this.mobileLegendId = mobileLegendId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public TelegramAccount nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Types getTypes() {
        return this.types;
    }

    public TelegramAccount types(Types types) {
        this.setTypes(types);
        return this;
    }

    public void setTypes(Types types) {
        this.types = types;
    }

    public Set<Pay> getPays() {
        return this.pays;
    }

    public void setPays(Set<Pay> pays) {
        if (this.pays != null) {
            this.pays.forEach(i -> i.setAccount(null));
        }
        if (pays != null) {
            pays.forEach(i -> i.setAccount(this));
        }
        this.pays = pays;
    }

    public TelegramAccount pays(Set<Pay> pays) {
        this.setPays(pays);
        return this;
    }

    public TelegramAccount addPay(Pay pay) {
        this.pays.add(pay);
        pay.setAccount(this);
        return this;
    }

    public TelegramAccount removePay(Pay pay) {
        this.pays.remove(pay);
        pay.setAccount(null);
        return this;
    }

    public InfoPaid getInfoPaid() {
        return this.infoPaid;
    }

    public void setInfoPaid(InfoPaid infoPaid) {
        this.infoPaid = infoPaid;
    }

    public TelegramAccount infoPaid(InfoPaid infoPaid) {
        this.setInfoPaid(infoPaid);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TelegramAccount)) {
            return false;
        }
        return id != null && id.equals(((TelegramAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TelegramAccount{" +
            "id=" + getId() +
            ", chatId=" + getChatId() +
            ", username='" + getUsername() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", role='" + getRole() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", mobileLegendId=" + getMobileLegendId() +
            ", nickName='" + getNickName() + "'" +
            ", types='" + getTypes() + "'" +
            "}";
    }
}
