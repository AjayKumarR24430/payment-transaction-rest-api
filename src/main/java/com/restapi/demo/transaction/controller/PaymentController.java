package com.restapi.demo.transaction.controller;

import com.restapi.demo.transaction.exception.AccountNotFoundException;
import com.restapi.demo.transaction.exception.InsufficientFundsException;
import com.restapi.demo.transaction.model.Payment;
import com.restapi.demo.transaction.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    /**
     * Constructs a new PaymentController with the given PaymentService.
     *
     * @param paymentService the PaymentService to use
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    /**
     * Makes a payment from one account to another.
     *
     * @param fromAccountId the ID of the account to transfer funds from
     * @param toAccountId the ID of the account to transfer funds to
     * @param amount the amount to transfer
     * @return a ResponseEntity containing the Payment object and a status code
     */
    public ResponseEntity<Payment> makePayment(@RequestParam String fromAccountId,
                                               @RequestParam String toAccountId,
                                               @RequestParam BigDecimal amount) {
        try {
            Payment payment = paymentService.makePayment(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok(payment);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
