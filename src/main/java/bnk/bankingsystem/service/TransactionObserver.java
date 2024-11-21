package bnk.bankingsystem.service;

import java.math.BigDecimal;

public interface TransactionObserver {
    void onTransaction(String accountNumber, String transactionType, BigDecimal total);
}
