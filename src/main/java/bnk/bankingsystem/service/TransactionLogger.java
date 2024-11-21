package bnk.bankingsystem.service;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TransactionLogger implements TransactionObserver {
    private static final Logger logger = Logger.getLogger(TransactionLogger.class.getName());
    private static final String LOG_FILE = "transactions.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onTransaction(String accountNumber, String transactionType, BigDecimal total) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("Timestamp: %s, Account: %s, Transaction: %s, Amount: %.2f",
                timestamp, accountNumber, transactionType, total);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to transaction log", e);
        }

        logger.info(logEntry);
    }
}

