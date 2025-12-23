package banking.repository;

import banking.domain.account.Account;
import banking.domain.transaction.Transaction;

import java.util.List;

public interface TransactionRepository {

    void saveTransaction(Transaction transaction);
    List<Transaction> findByAccountNumber(String accountNumber);
}
