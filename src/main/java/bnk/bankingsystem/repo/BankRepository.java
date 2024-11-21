package bnk.bankingsystem.repo;

import bnk.bankingsystem.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> getBankAccountByAccountNumber(String accountNumber);
}
