/**
 * PURPOSE:
 * Coordinates real-world banking workflows by orchestrating
 * repositories and domain operations.
 *
 * RESPONSIBILITIES:
 * - Ensure required objects (accounts) exist
 * - Handle application-level concerns (missing accounts, orchestration)
 * - Delegate business rule enforcement to the domain layer
 * - Persist resulting transactions
 *
 * Analogy:
 * - BankingService makes sure the right things happen in the right order
 *
 * DOES NOT:
 * - Enforce business rules directly
 * - Calculate balances
 * - Decide if an operation is financially valid
 * - Store data itself
 *
 * USED BY:
 * - Application entry points (UI, API, controllers)
 *
 * WHY THIS EXISTS:
 * Separates workflow coordination from business logic and persistence,
 * ensuring clear boundaries between layers and enabling testability.
 */


package banking.service;

import banking.domain.account.Account;
import banking.domain.operation.BankingOperation;
import banking.domain.transaction.Transaction;
import banking.exception.AccountNotFoundException;
import banking.repository.AccountRepository;
import banking.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

public class BankingService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BankingOperation bankingOperation;

     public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository, BankingOperation bankingOperation) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.bankingOperation = bankingOperation;
    }

    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findAccount(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountNumber
            );
        }

        Transaction transaction = bankingOperation.deposit(account, amount);
        transactionRepository.saveTransaction(transaction);
        return transaction;
    }

    public Transaction withdraw(String accountNumber, BigDecimal amount) {
         Account account = accountRepository.findAccount(accountNumber);

         if (account == null) {
             throw new AccountNotFoundException("Account not found: " + accountNumber);
         }

         Transaction transaction = bankingOperation.withdraw(account, amount, transactionRepository.findByAccountNumber(accountNumber));
         transactionRepository.saveTransaction(transaction);
         return transaction;
    }

    public List<Transaction> transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
         Account fromAccount = accountRepository.findAccount(fromAccountNumber);

         if (fromAccount == null) {
             throw new AccountNotFoundException("Account not found: " + fromAccountNumber);
         }

         Account toAccount = accountRepository.findAccount(toAccountNumber);

         if (toAccount == null) {
             throw new AccountNotFoundException("Account not found: " + toAccountNumber);
         }

         List<Transaction> transactionList = bankingOperation.transfer(fromAccount, toAccount, amount, transactionRepository.findByAccountNumber(fromAccountNumber));

         for(Transaction transaction : transactionList) {
             transactionRepository.saveTransaction(transaction);
         }

         return transactionList;
    }

}
