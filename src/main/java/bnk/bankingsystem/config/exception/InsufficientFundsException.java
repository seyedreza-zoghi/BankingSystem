package bnk.bankingsystem.config.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountNumber, BigDecimal amount) {
        super("Insufficient funds in the account with account number: " + accountNumber +
                ". Requested transfer amount: " + amount);
    }
}
