package bnk.bankingsystem.service;

import bnk.bankingsystem.config.exception.BankAccountNotFoundException;
import bnk.bankingsystem.config.exception.InsufficientFundsException;
import bnk.bankingsystem.enums.EN_OperationType;
import bnk.bankingsystem.model.BankAccount;
import bnk.bankingsystem.repo.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankService implements IBankService {

    private final BankRepository bankRepository;
    private final TransactionObserver transactionObserver;

    /**
     * @param customerName this name user
     * @param currentBalance this current balance
     * @return persis account
     */
    @Override
    @Transactional
    public BankAccount createAccount(String customerName, BigDecimal currentBalance) {
        if (currentBalance == null) currentBalance = BigDecimal.ZERO;
        var bankAccount = new BankAccount(customerName, currentBalance);
        log.info("make new user is accountNumber {}",bankAccount.getAccountNumber());
        BankAccount account = bankRepository.save(bankAccount);
        transactionObserver.onTransaction(account.getAccountNumber(), EN_OperationType.CREATE.name(),account.getCurrentBalance());
        return account;
    }

    /**
     * store : this method is use to deposit from the person's account
     * @param accountNumber The current user
     * @param amount deposit
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED , propagation = Propagation.REQUIRED)
    public void deposit(String accountNumber, BigDecimal amount) {
        var account = bankRepository.getBankAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankAccountNotFoundException(accountNumber));
        log.trace("this account number id is {}",account.getId());
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        BankAccount bankAccount = bankRepository.save(account);
        transactionObserver.onTransaction(bankAccount.getAccountNumber(), EN_OperationType.DEPOSIT.name(),bankAccount.getCurrentBalance());
    }

    /**
     * store : this method is use to withdraw from the person's account
     * @param accountNumber The current user
     * @param amount withdraw
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED , propagation = Propagation.REQUIRED)
    public void withdraw(String accountNumber, BigDecimal amount) {
        var account = bankRepository.getBankAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankAccountNotFoundException(accountNumber));
        log.trace("this account number id is {}",account.getId());
        if (account.getCurrentBalance().compareTo(amount) > 0 ) {
            log.info("this total is positive {}",account.getCurrentBalance());
            account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
            BankAccount bankAccount = bankRepository.save(account);
            transactionObserver.onTransaction(bankAccount.getAccountNumber(), EN_OperationType.WITHDRAW.name(),bankAccount.getCurrentBalance());

        } else
            throw new InsufficientFundsException(account.getAccountNumber(),amount);
    }

    /**
     * store : This method is used to transfer money between two accounts, and as a result, the transfer lock is registered
     * @param fromAccount The current user
     * @param toAccount The person we want to do
     * @param amount Transferable amount
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED , propagation = Propagation.REQUIRED)
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        var sourceAccount = bankRepository.getBankAccountByAccountNumber(fromAccount)
                .orElseThrow(() -> new BankAccountNotFoundException( fromAccount));

        var targetAccount = bankRepository.getBankAccountByAccountNumber(toAccount)
                .orElseThrow(() -> new BankAccountNotFoundException(toAccount));

        if (sourceAccount.getCurrentBalance().compareTo(amount) >= 0) {
            sourceAccount.setCurrentBalance(sourceAccount.getCurrentBalance().subtract(amount));
            targetAccount.setCurrentBalance(targetAccount.getCurrentBalance().add(amount));
            log.info("source account balance is {}, and target account {} balance for transfer ",sourceAccount.getCurrentBalance(),targetAccount.getCurrentBalance());
            BankAccount sourceAccountPersist = bankRepository.save(sourceAccount);
            transactionObserver.onTransaction(sourceAccountPersist.getAccountNumber(), EN_OperationType.TRANSFER.name(),sourceAccountPersist.getCurrentBalance());
            BankAccount targetAccountPersist = bankRepository.save(targetAccount);
            transactionObserver.onTransaction(targetAccountPersist.getAccountNumber(), EN_OperationType.TRANSFER.name(),targetAccountPersist.getCurrentBalance());
        } else {
            throw new InsufficientFundsException(sourceAccount.getAccountNumber(),amount);
        }
    }

    /**
     * store : is existing in to storage
     * @param accountNumber user exist
     * @return a account user
     */
    @Override
    @Transactional(readOnly = true)
    public BankAccount getBalanceAccountByAccountNumber(String accountNumber) {
        return bankRepository.getBankAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankAccountNotFoundException(accountNumber));
    }
    @Transactional
    public void performTransaction(String accountNumber, String type, BigDecimal total) {
        BankAccount account = bankRepository.getBankAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankAccountNotFoundException(accountNumber));

        switch (type.toLowerCase()) {
            case "deposit":
                deposit(accountNumber, total);
                break;
            case "withdraw":
                withdraw(accountNumber, total);
                break;
            default:
                throw new IllegalArgumentException("Unsupported transaction type");
        }
        bankRepository.save(account);
    }
}