package com.restapi.demo.transaction.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a payment entity in the database, with information about the sender, receiver, amount, and direction of the payment.
 */
@Entity
@Table(name = "payments")
public class Payment {

    public Payment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "from_account", nullable = false)
    private String fromAccount;

    @Column(name = "to_account", nullable = false)
    private String toAccount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /**
     * Returns the unique identifier of the payment.
     *
     * @return the payment ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the payment.
     *
     * @param id the payment ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the account ID of the sender of the payment.
     *
     * @return the account ID of the sender
     */
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * Sets the account ID of the sender of the payment.
     *
     * @param fromAccount the account ID of the sender to set
     */
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * Returns the account ID of the receiver of the payment.
     *
     * @return the account ID of the receiver
     */
    public String getToAccount() {
        return toAccount;
    }

    /**
     * Sets the account ID of the receiver of the payment.
     *
     * @param toAccount the account ID of the receiver to set
     */
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * Returns the amount of the payment.
     *
     * @return the payment amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the payment.
     *
     * @param amount the payment amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Returns the direction of the payment, i.e. whether it was incoming or outgoing.
     *
     * @return the payment direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the payment, i.e. whether it was incoming or outgoing.
     *
     * @param direction the payment direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Column(name = "direction", nullable = false)
    private String direction;
}
