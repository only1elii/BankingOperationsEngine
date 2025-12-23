package banking.domain.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {
    private final TransactionType transactionType;
    private final String transactionId;
    private final BigDecimal transactionAmount;
    private final Instant occurredAt;
    private final String transferId;
    private final String accountNumber;

    public Transaction(String accountNumber, Instant occurredAt, TransactionType transactionType, BigDecimal transactionAmount, String transactionId, String transferId) {
        this.accountNumber = accountNumber;
        this.occurredAt = occurredAt;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionId = transactionId;
        this.transferId = transferId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getTransferId() {
        return transferId;
    }
}
