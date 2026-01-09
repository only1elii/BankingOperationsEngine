package banking.domain.balance;

import banking.domain.transaction.Transaction;
import banking.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class BalanceCalculator {
    public static BigDecimal calculateBalance(String accountNumber, List<Transaction> transactions) {
        BigDecimal balance = BigDecimal.valueOf(0);

        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(accountNumber)) {
                if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
                    balance = balance.add(transaction.getTransactionAmount());
                } else if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                    balance = balance.subtract(transaction.getTransactionAmount());
                } else if (transaction.getTransactionType() == TransactionType.TRANSFER_CREDIT) {
                    balance = balance.add(transaction.getTransactionAmount());
                } else if (transaction.getTransactionType() == TransactionType.TRANSFER_DEBIT) {
                    balance = balance.subtract(transaction.getTransactionAmount());
                }
            }

        }
        return balance;
    }
}
