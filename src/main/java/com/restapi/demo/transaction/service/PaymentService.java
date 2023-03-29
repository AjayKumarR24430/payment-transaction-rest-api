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

/**
 * Service class for handling payments between accounts.
 */
@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    /**
     * Constructs a PaymentService instance with the given PaymentRepository and AccountRepository.
     *
     * @param paymentRepository The PaymentRepository to use for persisting payments.
     * @param accountRepository The AccountRepository to use for retrieving and updating accounts.
     */
    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Makes a payment from one account to another.
     *
     * @param fromAccountId The ID of the account to transfer funds from.
     * @param toAccountId The ID of the account to transfer funds to.
     * @param amount The amount to transfer.
     * @return The Payment object representing the transfer.
     * @throws AccountNotFoundException If either the fromAccountId or the toAccountId is not found.
     * @throws InsufficientFundsException If the fromAccountId has insufficient funds to complete the transfer.
     * @throws InvalidAccountException If the fromAccountId and the toAccountId are the same.
     */
    public Payment makePayment(String fromAccountId, String toAccountId, BigDecimal amount)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAccountException {

        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidAccountException("From account and to account cannot be the same.");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + toAccountId));

        BigDecimal fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient balance in account with id: " + fromAccountId);
        }

        fromAccount.setBalance(fromAccountBalance.subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        Payment payment = new Payment();
        payment.setFromAccount(fromAccountId);
        payment.setToAccount(toAccountId);
        payment.setAmount(amount);
        payment.setDirection("OUTGOING");

        paymentRepository.save(payment);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return payment;
    }
}
