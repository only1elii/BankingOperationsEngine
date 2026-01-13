/**
 * PURPOSE:
 * Encapsulates core banking business rules and domain logic.
 *
 * RESPONSIBILITIES:
 * - Validate operation legality (state rules, amounts, balances)
 * - Create domain transactions (deposit, withdrawal, transfer)
 * - Enforce invariants (no overdrafts, valid state transitions)
 *
 * DOES NOT:
 * - Check if accounts exist
 * - Persist transactions
 * - Interact with repositories
 * - Coordinate workflows
 *
 * USED BY:
 * - BankingService
 *
 * WHY THIS EXISTS:
 * Keeps business rules centralized, testable, and independent
 * of persistence or application-level concerns.
 */


package banking.domain.operation;

import banking.domain.account.Account;
import banking.domain.balance.BalanceCalculator;
import banking.domain.transaction.Transaction;
import banking.domain.transaction.TransactionType;
import banking.exception.InsufficientFundsException;
import banking.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankingOperation {

    public void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
    }

    public Transaction deposit(Account account, BigDecimal amount) {
        validateAmount(amount);
        account.assertCanDeposit();

        // Creates random transaction ID.
        String transactionId = UUID.randomUUID().toString();

        Transaction tx = new Transaction(
                account.getAccountNumber(),
                Instant.now(),
                TransactionType.DEPOSIT,
                amount,
                transactionId,
                null
        );

        return tx;
    }

    public Transaction withdraw(Account account, BigDecimal amount, List<Transaction> existingTransactions) {
        validateAmount(amount);
        account.assertCanWithdraw();
        String transactionId = UUID.randomUUID().toString();
        BigDecimal balance = BalanceCalculator.calculateBalance(account.getAccountNumber(), existingTransactions);

        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException("Insufficient funds: Withdrawal of $ " + amount + " available balance of $" + balance + ".");
        }

        Transaction tx = new Transaction(
                account.getAccountNumber(),
                Instant.now(),
                TransactionType.WITHDRAW,
                amount,
                transactionId,
                null
        );

        return tx;
    }

    public List<Transaction> transfer(Account fromAccount, Account toAccount, BigDecimal amount, List<Transaction> existingTransactions) {
        validateAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        fromAccount.assertCanTransfer();
        toAccount.assertCanDeposit();
        String fromTransactionId = UUID.randomUUID().toString();
        String toTransactionId = UUID.randomUUID().toString();
        String transferId = UUID.randomUUID().toString();
        Instant occuredAt = Instant.now();

        BigDecimal balance = BalanceCalculator.calculateBalance(fromAccount.getAccountNumber(), existingTransactions);

        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException("Insufficient funds: Transfer of $ " + amount + " available balance of $" + balance + ".");
        }

        Transaction fromTx = new Transaction(
                fromAccount.getAccountNumber(),
                occuredAt,
                TransactionType.TRANSFER_DEBIT,
                amount,
                fromTransactionId,
                transferId
        );

        Transaction toTx = new Transaction(
                toAccount.getAccountNumber(),
                occuredAt,
                TransactionType.TRANSFER_CREDIT,
                amount,
                toTransactionId,
                transferId
        );

        transactions.add(fromTx);
        transactions.add(toTx);

        return transactions;
    }

}
