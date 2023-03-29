package com.restapi.demo.transaction;

import com.restapi.demo.transaction.exception.*;
import com.restapi.demo.transaction.model.Account;
import com.restapi.demo.transaction.model.Payment;
import com.restapi.demo.transaction.repository.AccountRepository;
import com.restapi.demo.transaction.repository.PaymentRepository;
import com.restapi.demo.transaction.service.AccountService;
import com.restapi.demo.transaction.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionApplicationTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @InjectMocks
    private AccountService accountService;

    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private Account existingAccount;
    private Account updatedAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        fromAccount = new Account();
        fromAccount.setId("1");
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        toAccount = new Account();
        toAccount.setId("2");
        toAccount.setBalance(BigDecimal.valueOf(500));

        existingAccount = new Account();
        existingAccount.setId("1");
        existingAccount.setOwner("ABC");
        existingAccount.setBalance(BigDecimal.valueOf(1000));

        updatedAccount = new Account();
        updatedAccount.setId("1");
        updatedAccount.setOwner("DEF");
        updatedAccount.setBalance(BigDecimal.valueOf(500));
    }

    // AccountService tests
    @Test
    public void updateAccount_shouldUpdateAccountSuccessfully() throws Exception {
        // Arrange
        when(accountRepository.findById("1")).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        // Act
        Account account = accountService.updateAccount("1", updatedAccount);

        // Assert
        Assertions.assertNotNull(account);
        Assertions.assertEquals("Mary", account.getOwner());
        Assertions.assertEquals(BigDecimal.valueOf(500), account.getBalance());
    }

    @Test
    public void updateAccount_shouldThrowAccountNotFoundException_whenAccountNotFound() {
        // Arrange
        when(accountRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateAccount("1", updatedAccount);
        });
    }

    @Test
    public void updateAccount_shouldThrowInvalidAccountException_whenAccountIdIsNull() {
        // Act & Assert
        Assertions.assertThrows(InvalidAccountException.class, () -> {
            accountService.updateAccount(null, updatedAccount);
        });
    }

    @Test
    public void updateAccount_shouldThrowInvalidAccountException_whenAccountIdIsEmpty() {
        // Act & Assert
        Assertions.assertThrows(InvalidAccountException.class, () -> {
            accountService.updateAccount("", updatedAccount);
        });
    }

    // PaymentService tests
    @Test
    public void makePayment_shouldTransferMoneySuccessfully() throws Exception {

        when(accountRepository.findById("1")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("2")).thenReturn(Optional.of(toAccount));

        // Act
        Payment payment = paymentService.makePayment("1", "2", amount);

        // Assert
        Assertions.assertNotNull(payment);
        Assertions.assertEquals(fromAccount.getBalance(), BigDecimal.valueOf(900));
        Assertions.assertEquals(toAccount.getBalance(), BigDecimal.valueOf(600));
    }

    @Test
    public void makePayment_shouldThrowAccountNotFoundException_whenFromAccountNotFound() {

        when(accountRepository.findById("1")).thenReturn(Optional.empty());
        when(accountRepository.findById("2")).thenReturn(Optional.of(toAccount));

        // Act & Assert
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            paymentService.makePayment("1", "2", amount);
        });
    }

    @Test
    public void makePayment_shouldThrowAccountNotFoundException_whenToAccountNotFound() {

        when(accountRepository.findById("1")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("2")).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            paymentService.makePayment("1", "2", amount);
        });
    }

    @Test
    public void makePayment_shouldThrowInsufficientFundsException_whenBalanceIsNotEnough() {

        fromAccount.setBalance(BigDecimal.valueOf(50));
        when(accountRepository.findById("1")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("2")).thenReturn(Optional.of(toAccount));

        // Act & Assert
        Assertions.assertThrows(InsufficientFundsException.class, () -> {
            paymentService.makePayment("1", "2", amount);
        });
    }
}

