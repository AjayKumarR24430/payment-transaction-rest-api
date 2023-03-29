package com.restapi.demo.transaction.repository;

import com.restapi.demo.transaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Returns a list of all accounts in the database.
     *
     * @return A list of all accounts in the database.
     */
    List<Account> findAll();

    /**
     * Finds the account with the specified ID in the database.
     *
     * @param id The ID of the account to find.
     * @return An optional containing the account with the specified ID if it exists,
     *         or an empty optional if it does not.
     */
    Optional<Account> findById(String id);
}
