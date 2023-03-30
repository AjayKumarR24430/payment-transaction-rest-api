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
    @ExceptionHandler({AccountNotFoundException.class, InvalidAccountException.class})
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
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
    @ExceptionHandler({AccountNotFoundException.class, InvalidAccountException.class})
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
    @ExceptionHandler({AccountNotFoundException.class, InvalidAccountException.class})
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deposits funds into the account with the specified ID.
     *
     * @param accountId the ID of the account to deposit funds into
     * @param amount    the amount of funds to deposit
     * @return a ResponseEntity with an HTTP status code indicating success or failure
     */
    @ExceptionHandler({AccountNotFoundException.class, InvalidAccountException.class, InsufficientFundsException.class})
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable String accountId, @RequestParam("amount") BigDecimal amount) {
        accountService.deposit(accountId, amount);
        return ResponseEntity.noContent().build();
    }

    /**
     * Withdraws funds from the account
     *
     * @param accountId The ID of the account from which to withdraw funds
     * @param amount The amount to withdraw from the account
     * @return A ResponseEntity object containing the HTTP status code and any relevant data
     * @throws AccountNotFoundException if the specified account is not found
     * @throws InsufficientFundsException if the account balance is not sufficient to cover the withdrawal amount
     * @throws InvalidAccountException if the specified account ID is invalid
     */
    @ExceptionHandler({AccountNotFoundException.class, InvalidAccountException.class, InsufficientFundsException.class})
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountId, @RequestParam("amount") BigDecimal amount)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAccountException {
        try {
            accountService.withdraw(accountId, amount);
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InvalidAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
