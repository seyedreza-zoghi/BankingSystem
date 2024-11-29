package bnk.bankingsystem;

import bnk.bankingsystem.enums.EN_OperationType;
import bnk.bankingsystem.model.BankAccount;
import bnk.bankingsystem.repo.BankRepository;
import bnk.bankingsystem.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BankingSystemApplicationTests {

    @Autowired
    private BankService bankService;

    @Autowired
    private BankRepository bankRepository;

    @BeforeEach
    public void setUp() {
        BankAccount account = bankService.createAccount("ACC-12345", new BigDecimal("1000.00"));
    }

    @Test
    public void testConcurrentTransactions() throws InterruptedException {
        BankAccount bankAccountByCustomerName = bankRepository.getBankAccountByCustomerName("ACC-12345");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> bankService.performTransaction(bankAccountByCustomerName.getAccountNumber(), EN_OperationType.DEPOSIT.name(), new BigDecimal("500.00")));
        executor.submit(() -> bankService.performTransaction(bankAccountByCustomerName.getAccountNumber(), EN_OperationType.WITHDRAW.name(), new BigDecimal("100.00")));

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        BigDecimal finalBalance = bankService.getBalanceAccountByAccountNumber(bankAccountByCustomerName.getAccountNumber()).getCurrentBalance();
        assertEquals(new BigDecimal("1400.00"), finalBalance);
    }

}
