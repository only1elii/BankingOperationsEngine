package banking.repository.memory;

import banking.domain.transaction.Transaction;
import banking.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions;

    public InMemoryTransactionRepository() {
        transactions = new ArrayList<>();
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> transactionList = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if(transaction.getAccountNumber().equals(accountNumber)) {
                transactionList.add(transaction);
            }
        }

        return transactionList;

    }
}
