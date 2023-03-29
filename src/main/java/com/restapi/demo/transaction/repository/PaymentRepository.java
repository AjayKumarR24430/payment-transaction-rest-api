package com.restapi.demo.transaction.repository;

import com.restapi.demo.transaction.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Returns a list of all payments in the database.
     *
     * @return a list of all payments in the database
     */
    List<Payment> findAll();

    /**
     * Returns an Optional containing the payment with the given ID, or an empty Optional if no payment is found.
     *
     * @param id the ID of the payment to retrieve
     * @return an Optional containing the payment with the given ID, or an empty Optional if no payment is found
     */
    Optional<Payment> findById(Long id);
}
