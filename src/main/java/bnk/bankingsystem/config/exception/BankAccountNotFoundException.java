package bnk.bankingsystem.config.exception;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String accountNumber) {
        super("Bank account with account number " + accountNumber + " not found.");
    }
}