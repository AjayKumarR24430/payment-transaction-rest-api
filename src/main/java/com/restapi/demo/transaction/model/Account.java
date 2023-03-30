package com.restapi.demo.transaction.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    public Account() {
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "currency", nullable = false)
    private String currency;

    /**
     * Returns the owner of the account.
     * @return The owner of the account.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the account.
     * @param owner The owner of the account.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the balance of the account.
     * @return The balance of the account.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     * @param balance The balance of the account.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Returns the currency of the account.
     * @return The currency of the account.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the account.
     * @param currency The currency of the account.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Returns the ID of the account.
     * @return The ID of the account.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the account.
     * @param id The ID of the account.
     */
    public void setId(String id) {
        this.id = id;
    }
}
