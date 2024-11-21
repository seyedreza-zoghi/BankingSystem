package bnk.bankingsystem.service;

import bnk.bankingsystem.model.BankAccount;

import java.math.BigDecimal;

public interface IBankService {
    BankAccount createAccount(String customerName, BigDecimal currentBalance);
    void deposit(String accountNumber, BigDecimal amount);
    void withdraw(String accountNumber, BigDecimal amount);
    void transfer(String fromAccount, String toAccount, BigDecimal amount);
    BankAccount getBalanceAccountByAccountNumber(String accountNumber);
    void performTransaction(String accountNumber, String type, BigDecimal total);
}
