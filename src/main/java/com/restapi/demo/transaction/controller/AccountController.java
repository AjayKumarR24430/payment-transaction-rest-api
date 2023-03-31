package com.restapi.demo.transaction.controller;

import com.restapi.demo.transaction.exception.*;
import com.restapi.demo.transaction.model.Account;
import com.restapi.demo.transaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Returns a list of all accounts.
     *
     * @return a ResponseEntity containing a list of accounts and an HTTP status code
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Returns the account with the specified ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return a ResponseEntity containing the account and an HTTP status code
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    /**
     * Creates a new account.
     *
     * @param account the account to create
     * @return a ResponseEntity containing the created account and an HTTP status code
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    /**
     * Updates the account with the specified ID.
     *
     * @param accountId the ID of the account to update
     * @param account   the updated account object
     * @return a ResponseEntity containing the updated account and an HTTP status code
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountId, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(accountId, account);
        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Deletes the account with the specified ID.
     *
     * @param accountId the ID of the account to delete
     * @return a ResponseEntity with an HTTP status code indicating success or failure
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * Deposits funds from one account to another.
     * @param fromAccountId the ID of the account to transfer funds from
     * @param depositRequest a DepositRequest object containing the ID of the account to transfer funds to and the amount to transfer
     * @return a ResponseEntity with an HTTP status code indicating success or failure
     */
    @PostMapping("/{fromAccountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String fromAccountId, @RequestBody DepositRequest depositRequest) {
        try {
            accountService.deposit(depositRequest.getToAccountId(), depositRequest.getAmount(), fromAccountId);
            String message = String.format("Successfully transferred %s from account %s to account %s.", depositRequest.getAmount(), fromAccountId, depositRequest.getToAccountId());
            return ResponseEntity.ok(message);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Insufficient funds.");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Withdraws funds from the account
     *
     * @param withdrawRequest A WithdrawRequest object containing the ID of the account from which to withdraw funds and the amount to withdraw
     * @return A ResponseEntity object containing the HTTP status code and any relevant data
     * @throws AccountNotFoundException if the specified account is not found
     * @throws InsufficientFundsException if the account balance is not sufficient to cover the withdrawal amount
     * @throws InvalidAccountException if the specified account ID is invalid
     */
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountId, @RequestBody WithdrawRequest withdrawRequest)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAccountException {
        try {
            accountService.withdraw(accountId, withdrawRequest.getAmount());
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public static class DepositRequest {
        private String toAccountId;
        private BigDecimal amount;

        public DepositRequest() {
        }

        public DepositRequest(String toAccountId, BigDecimal amount) {
            this.toAccountId = toAccountId;
            this.amount = amount;
        }

        public String getToAccountId() {
            return toAccountId;
        }

        public void setToAccountId(String toAccountId) {
            this.toAccountId = toAccountId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class WithdrawRequest {
        private BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<String> handleInvalidAccountException(InvalidAccountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
