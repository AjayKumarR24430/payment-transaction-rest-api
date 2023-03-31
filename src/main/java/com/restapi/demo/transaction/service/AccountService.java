package com.restapi.demo.transaction.service;

import com.restapi.demo.transaction.exception.AccountNotFoundException;
import com.restapi.demo.transaction.exception.InsufficientFundsException;
import com.restapi.demo.transaction.exception.InvalidAccountException;
import com.restapi.demo.transaction.model.Account;
import com.restapi.demo.transaction.model.Payment;
import com.restapi.demo.transaction.repository.AccountRepository;
import com.restapi.demo.transaction.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling account-related transactions.
 */
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final PaymentRepository paymentRepository;

    /**
     * Constructs a new instance of the AccountService class with the specified AccountRepository and PaymentRepository.
     *
     * @param accountRepository The AccountRepository to use for accessing account data.
     * @param paymentRepository The PaymentRepository to use for accessing payment data.
     */
    @Autowired
    public AccountService(AccountRepository accountRepository, PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Retrieves the account with the specified ID.
     *
     * @param accountId The ID of the account to retrieve.
     * @return The account with the specified ID.
     * @throws AccountNotFoundException If no account exists with the specified ID.
     * @throws InvalidAccountException  If the specified account ID is null or empty.
     */
    public Account getAccountById(String accountId) throws AccountNotFoundException, InvalidAccountException {
        if (accountId == null || accountId.isEmpty()) {
            throw new InvalidAccountException("Account id cannot be null or empty");
        }
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        throw new AccountNotFoundException("Account not found");
    }

    /**
     * Retrieves a list of all accounts.
     *
     * @return A list of all accounts.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Creates a new account.
     *
     * @param account The account to create.
     * @return The newly created account.
     */
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Updates an existing account.
     *
     * @param accountId      The ID of the account to update.
     * @param updatedAccount The updated account data.
     * @return The updated account.
     * @throws AccountNotFoundException If no account exists with the specified ID.
     * @throws InvalidAccountException  If the specified account ID is null or empty.
     */
    public Account updateAccount(String accountId, Account updatedAccount) throws AccountNotFoundException, InvalidAccountException {
        if (accountId == null || accountId.isEmpty()) {
            throw new InvalidAccountException("Account id cannot be null or empty");
        }
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setOwner(updatedAccount.getOwner());
            account.setBalance(updatedAccount.getBalance());
            return accountRepository.save(account);
        }
        throw new AccountNotFoundException("Account not found");
    }

    /**
     * Deletes the account with the specified ID.
     *
     * @param accountId The ID of the account to delete.
     * @throws AccountNotFoundException If no account exists with the specified ID.
     * @throws InvalidAccountException  If the specified account ID is null or empty.
     */
    public void deleteAccount(String accountId) throws AccountNotFoundException, InvalidAccountException {
        if (accountId == null || accountId.isEmpty()) {
            throw new InvalidAccountException("Account id cannot be null or empty");
        }
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            accountRepository.delete(account);
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    /**
     * Withdraws the specified amount from the account with the specified ID.
     *
     * @param accountId The ID of the account to withdraw from.
     * @param amount    The amount to withdraw.
     * @throws AccountNotFoundException   If no account exists with the specified ID.
     * @throws InvalidAccountException    If the specified account ID is null or empty.
     * @throws InsufficientFundsException If the account balance is less than the specified amount.
     */
    public synchronized void withdraw(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAccountException, InsufficientFundsException {
        if (accountId == null || accountId.isEmpty()) {
            throw new InvalidAccountException("Account id cannot be null or empty");
        }
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getBalance().compareTo(amount) >= 0) {
                account.setBalance(account.getBalance().subtract(amount));
                accountRepository.save(account);
            } else {
                throw new InsufficientFundsException("Insufficient funds in account");
            }
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }

    /**
     * Deposits the specified amount into the account with the specified ID.
     *
     * @param accountId The ID of the account to deposit into.
     * @param amount    The amount to deposit.
     * @param fromAccountId The ID of the account that the funds are being transferred from.
     * @throws AccountNotFoundException If no account exists with the specified ID.
     * @throws InvalidAccountException  If the specified account ID is null or empty.
     */
    public synchronized void deposit(String accountId, BigDecimal amount, String fromAccountId) throws AccountNotFoundException, InvalidAccountException, InsufficientFundsException {
        if (accountId == null || accountId.isEmpty()) {
            throw new InvalidAccountException("Account id cannot be null or empty");
        }
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            BigDecimal newBalance = account.getBalance().add(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            account.setBalance(newBalance);
            accountRepository.save(account);

            // Create a new Payment object
            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setToAccount(accountId);
            payment.setDirection("incoming");

            // Check if there is a from account specified
            if (fromAccountId != null && !fromAccountId.isEmpty()) {
                Optional<Account> optionalFromAccount = accountRepository.findById(fromAccountId);
                if (optionalFromAccount.isPresent()) {
                    Account fromAccount = optionalFromAccount.get();
                    BigDecimal newFromAccountBalance = fromAccount.getBalance().subtract(amount);
                    if (newFromAccountBalance.compareTo(BigDecimal.ZERO) < 0) {
                        throw new InsufficientFundsException("Insufficient funds");
                    }
                    fromAccount.setBalance(newFromAccountBalance);
                    accountRepository.save(fromAccount);
                    payment.setFromAccount(fromAccountId);
                    payment.setDirection("outgoing");
                }
            }
            // Save the Payment object to the database
            paymentRepository.save(payment);
        } else {
            throw new AccountNotFoundException("Account not found");
        }
    }


}