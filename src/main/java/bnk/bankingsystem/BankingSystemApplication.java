package bnk.bankingsystem;

import bnk.bankingsystem.model.BankAccount;
import bnk.bankingsystem.service.IBankService;
import bnk.bankingsystem.service.TransactionLogger;
import bnk.bankingsystem.service.TransactionObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
public class BankingSystemApplication implements CommandLineRunner {

    private final IBankService bankAccount;
    private final TransactionObserver transactionLogger;
    public static void main(String[] args) {
        SpringApplication.run(BankingSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        BankAccount ali = bankAccount.createAccount("Ali", new BigDecimal("1000.00"));
        BankAccount reza = bankAccount.createAccount("reza", new BigDecimal("0"));

        // واریز وجه
        BigDecimal input1 = new BigDecimal("500.00");
        bankAccount.deposit(ali.getAccountNumber(), input1);
        transactionLogger.onTransaction(ali.getAccountNumber(), "Deposit", input1);

        // برداشت وجه
        BigDecimal input2 = new BigDecimal("200.00");
        bankAccount.withdraw(ali.getAccountNumber(), input2);
        transactionLogger.onTransaction(ali.getAccountNumber(), "Withdraw", input2);

        // انتقال وجه
        BigDecimal input3 = new BigDecimal("100.00");
        bankAccount.transfer(ali.getAccountNumber(), reza.getAccountNumber(), input3);
        transactionLogger.onTransaction(ali.getAccountNumber(), reza.getAccountNumber(), input3);
    }
}
