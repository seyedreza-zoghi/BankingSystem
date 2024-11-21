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

        bankAccount.createAccount("Ali", new BigDecimal("1000.00"));

        // واریز وجه
        BigDecimal input1 = new BigDecimal("500.00");
        bankAccount.deposit("ACC-12345", input1);
        transactionLogger.onTransaction("ACC-12345", "Deposit", input1);

        // برداشت وجه
        BigDecimal input2 = new BigDecimal("200.00");
        bankAccount.withdraw("ACC-12345", input2);
        transactionLogger.onTransaction("ACC-12345", "Withdraw", input2);

        // انتقال وجه
        BigDecimal input3 = new BigDecimal("100.00");
        bankAccount.transfer("ACC-12345", "ACC-67890", input3);
        transactionLogger.onTransaction("ACC-12345", "Transfer", input3);
    }
}
