/**
 * PURPOSE:
 * Acts as an in-memory transaction ledger that permanently records immutable financial events and allows them to be queried later
 *
 * RESPONSIBILITIES:
 * - Persist transactions
 * - Retrieve transactions by accountNumber
 *
 * Analogy:
 * - A transaction history notebook
 * - Everytime a transaction happens, a new line is written permanently in that notebook. Can always refer back to it
 *
 * DOES NOT:
 * - Calculate balances
 * - Enforce rules
 * - Decide if a transaction is valid
 *
 * USED BY:
 * - BankingService (to persist results)
 * - BalanceCalculator (indirectly, via service)
 *
 * WHY THIS EXISTS:
 * Allows transaction history to be stored and queried
 * without tying the system to a database.
 */


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
