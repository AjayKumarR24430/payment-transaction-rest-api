# Transaction API
The Transaction API provides endpoints for managing user accounts and making payments between accounts. It includes packages for models, repositories, services, controllers, and exceptions.

## Purpose
The Transaction API is designed to facilitate the transfer of funds between user accounts. Users can create new accounts, deposit funds into existing accounts, and make payments to other users. The API handles account and payment validation, error handling, and database interactions.

## Technologies Used
The Transaction API is built using Java and the Spring Framework. The project includes the following dependencies:

* Spring Boot Starter Web
* Spring Boot Starter Data JPA
* MySQL database
* Spring Boot Starter Test

## Setup
To set up the project, follow these steps:

* Clone the repository to your local machine.
* Open the project in your preferred IDE.
* Build the project using Maven or Gradle.
* Start the application using the main method in the TransactionApplication class.
* The application will run on localhost:8080 by default.

## Endpoints
The Transaction API includes the following endpoints:

### Accounts

#### GET /v1/accounts/{accountId} 
Retrieves information about the specified account.

#### POST /v1/accounts
Creates a new account with the specified information.

#### PUT /v1/accounts/{accountId}/deposit?amount={amount}
Deposits the specified amount into the specified account.

#### PUT /v1/accounts/{accountId}/withdraw?amount={amount}
Withdraws the specified amount from the specified account.

### Payments

#### POST /v1/payments?fromAccountId={fromAccountId}&toAccountId={toAccountId}&amount={amount}
Initiates a payment from the specified account to the specified recipient account for the specified amount.

## Testing
The Transaction API includes a suite of automated tests that can be run using the test command in Maven or Gradle. These tests cover various scenarios for account creation, depositing, withdrawing, and making payments.

## Linting
The Transaction API includes a set of linting rules that can be enforced using tools like Checkstyle or PMD. These rules are intended to ensure code consistency and maintainability.

## Database Transactions, Locks, and Race Conditions
The Transaction API includes database transactions and locks to ensure data consistency and prevent race conditions. Database transactions ensure that each database operation is executed atomically, either entirely or not at all. Locks prevent multiple users from modifying the same data simultaneously, ensuring that only one user has access to a particular record at any given time.

This project uses JPA and MySQL database. JPA supports database transactions, which ensure that all operations in a transaction are either committed to the database or rolled back if any operation fails. This helps to maintain data consistency in case of any errors.

To prevent race conditions when updating the same account balance simultaneously, the synchronized keyword is used in the AccountService class for the withdraw and deposit methods. This ensures that only one thread can access the method at a time, thereby preventing race conditions.

MySQL supports row-level locking, which allows multiple transactions to access different rows in a table simultaneously, while preventing access to the same row by multiple transactions at the same time. This helps to prevent race conditions when updating the same row simultaneously.