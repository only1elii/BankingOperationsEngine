package banking.service;

import banking.domain.account.Account;
import banking.domain.account.AccountState;
import banking.domain.operation.BankingOperation;
import banking.domain.transaction.Transaction;
import banking.domain.transaction.TransactionType;
import banking.exception.AccountNotFoundException;
import banking.exception.InsufficientFundsException;
import banking.repository.memory.InMemoryAccountRepository;
import banking.repository.memory.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankingServiceTest {

    @Test
    void deposit_CreatesAndStoresTransactions() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        BankingOperation bankingOperation = new BankingOperation();
        BankingService bankingService = new BankingService(
                accountRepository,
                transactionRepository,
                bankingOperation
        );

        Account account = new Account("A1", AccountState.ACTIVE);
        accountRepository.saveAccount(account);

        Transaction transaction =
                bankingService.deposit(
                        "A1",
                        new BigDecimal("100.00")
                );


        assertEquals(
                new BigDecimal("100.00"),
                transaction.getTransactionAmount()
        );

        assertEquals(
                1,
                transactionRepository
                        .findByAccountNumber("A1")
                        .size()
        );
    }

    @Test
    void deposit_AccountNotFoundException() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        BankingOperation bankingOperation = new BankingOperation();
        BankingService bankingService = new BankingService(
                accountRepository,
                transactionRepository,
                bankingOperation
        );

        assertThrows(AccountNotFoundException.class, () -> bankingService.deposit("A1", new BigDecimal("100")));

    }

    @Test
    void withDraw_AccountNotFoundException() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        BankingOperation bankingOperation = new BankingOperation();
        BankingService bankingService = new BankingService(
                accountRepository,
                transactionRepository,
                bankingOperation
        );

        assertThrows(AccountNotFoundException.class, () -> bankingService.withdraw("A1", new BigDecimal("100")));
    }


    @Test
    void withdraw_InsufficientFunds_ThrowsException() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        BankingOperation bankingOperation = new BankingOperation();
        BankingService bankingService = new BankingService(
                accountRepository,
                transactionRepository,
                bankingOperation
        );

        Account account = new Account("A1", AccountState.ACTIVE);
        accountRepository.saveAccount(account);



        assertThrows(InsufficientFundsException.class, () -> bankingService.withdraw("A1", new BigDecimal("100")));

    }

    @Test
    void withdraw_Success_createsTransaction() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        BankingOperation bankingOperation = new BankingOperation();
        BankingService bankingService = new BankingService(
                accountRepository,
                transactionRepository,
                bankingOperation
        );

        Account account = new Account("A1", AccountState.ACTIVE);
        accountRepository.saveAccount(account);
        bankingService.deposit("A1", new BigDecimal("100"));

        Transaction tx = bankingService.withdraw("A1", new BigDecimal("50"));

        assertEquals(TransactionType.WITHDRAW, tx.getTransactionType());
        assertEquals("A1", tx.getAccountNumber());
        assertEquals(new BigDecimal("50"), tx.getTransactionAmount());

    }

    @Test
    void transfer_Success_CreatesTwoLinkedTransactions() {
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        BankingOperation bankingOperation = new BankingOperation();
        BankingService bankingService = new BankingService(
                accountRepository,
                transactionRepository,
                bankingOperation
        );

        Account fromAccount = new Account("A1", AccountState.ACTIVE);
        Account toAccount = new Account("B1", AccountState.ACTIVE);
        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);
        bankingService.deposit("A1", new BigDecimal("500"));

        List<Transaction> transactions = bankingService.transfer("A1", "B1", new BigDecimal("400"));

        assertEquals(2, transactionRepository.findByAccountNumber("A1").size());
        assertEquals(1, transactionRepository.findByAccountNumber("B1").size());
        assertEquals(transactions.get(0).getTransferId(), transactions.getLast().getTransferId());
    }
}

