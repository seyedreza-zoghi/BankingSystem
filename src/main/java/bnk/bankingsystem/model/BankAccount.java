package bnk.bankingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vc_customer", indexes = {
        @Index(name = "indx__vc_account_number", columnList = "account_number", unique = true)
})
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ACCOUNT_NUMBER", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String customerName;

    @Column(name = "CURRENT_BALANCE", nullable = false)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    public BankAccount(String customerName, BigDecimal currentBalance) {
        this.accountNumber = generateAccountNumber();
        this.customerName = customerName;
        this.currentBalance = currentBalance;
    }

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID();
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BankAccount that = (BankAccount) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}


