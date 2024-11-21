package bnk.bankingsystem;

import bnk.bankingsystem.enums.EN_OperationType;
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

    @BeforeEach
    public void setUp() {
        bankService.createAccount("ACC-12345", new BigDecimal("1000.00"));
    }

    @Test
    public void testConcurrentTransactions() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> bankService.performTransaction("ACC-12345", EN_OperationType.DEPOSIT.name(), new BigDecimal("500.00")));
        executor.submit(() -> bankService.performTransaction("ACC-12345", EN_OperationType.WITHDRAW.name(), new BigDecimal("100.00")));

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        BigDecimal finalBalance = bankService.getBalanceAccountByAccountNumber("ACC-12345").getCurrentBalance();
        assertEquals(new BigDecimal("1400.00"), finalBalance);
    }

}
