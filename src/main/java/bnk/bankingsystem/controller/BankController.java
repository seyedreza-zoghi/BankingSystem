package bnk.bankingsystem.controller;

import bnk.bankingsystem.model.BankAccount;
import bnk.bankingsystem.service.IBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankController {

    private final IBankService bankService;
    @PostMapping("/accounts")
    public ResponseEntity<BankAccount> createAccount(@RequestParam String customerName,
                                                     @RequestParam(required = false) BigDecimal initialBalance) {
        BankAccount account = bankService.createAccount(customerName, initialBalance);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable String accountNumber,
                                        @RequestParam BigDecimal amount) {
        bankService.deposit(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable String accountNumber,
                                         @RequestParam BigDecimal amount) {
        bankService.withdraw(accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestParam String fromAccount,
                                         @RequestParam String toAccount,
                                         @RequestParam BigDecimal amount) {
        bankService.transfer(fromAccount, toAccount, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<BankAccount> getBalance(@PathVariable String accountNumber) {
        BankAccount account = bankService.getBalanceAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/accounts/{accountNumber}/transaction")
    public ResponseEntity<Void> performTransaction(@PathVariable String accountNumber,
                                                   @RequestParam String type,
                                                   @RequestParam BigDecimal amount) {
        bankService.performTransaction(accountNumber, type, amount);
        return ResponseEntity.ok().build();
    }

}
