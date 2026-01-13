package banking.domain.balance;

import banking.domain.transaction.Transaction;
import banking.domain.transaction.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceCalculatorTest {

    @Test
    void emptyLedger_ReturnsZero() {
        BigDecimal balance = BalanceCalculator.calculateBalance("A1", List.of());

        assertEquals(BigDecimal.ZERO, balance);

    }

    @Test
    void singleDeposit_IncreasesBalance() {
        Transaction tx = new Transaction(
                "1234",
                Instant.now(),
                TransactionType.DEPOSIT,
                new BigDecimal("100.00"),
                UUID.randomUUID().toString(),
                null
        );


        BigDecimal balance = BalanceCalculator.calculateBalance("1234", List.of(tx));

        assertEquals(new BigDecimal("100.00"), balance);

    }

    @Test
    void singleWithdraw_DecreasesBalance() {

        Transaction tx = new Transaction("1234", Instant.now(), TransactionType.WITHDRAW, new BigDecimal("100.00"), UUID.randomUUID().toString(), null);
        BigDecimal balance = BalanceCalculator.calculateBalance("1234", List.of(tx));

        assertEquals(new BigDecimal("-100.00"), balance);

    }

    @Test
    void mixedLedger_calculatesNetBalance() {
         List<Transaction> transactions = List.of(
                 new Transaction("1234", Instant.now(), TransactionType.DEPOSIT, new BigDecimal("1500.00"), UUID.randomUUID().toString(), null),
                 new Transaction("1234", Instant.now(), TransactionType.WITHDRAW, new BigDecimal("500"), UUID.randomUUID().toString(), null),
                 new Transaction("1234", Instant.now(), TransactionType.TRANSFER_DEBIT, new BigDecimal("300"), UUID.randomUUID().toString(), UUID.randomUUID().toString()),
                 new Transaction("1234", Instant.now(), TransactionType.TRANSFER_CREDIT, new BigDecimal("100"), UUID.randomUUID().toString(), UUID.randomUUID().toString())
         );

         BigDecimal balance = BalanceCalculator.calculateBalance("1234", transactions);

         assertEquals(new BigDecimal("800.00"), balance);

    }

    @Test
    void ignoresOtherAccounts() {
        // Concept: BalanceCalculator should ignore transactions for other accounts.
        List<Transaction> transactions = List.of(
                new Transaction("A1", Instant.now(), TransactionType.DEPOSIT,
                        new BigDecimal("100.00"), UUID.randomUUID().toString(), null),
                new Transaction("A2", Instant.now(), TransactionType.DEPOSIT,
                        new BigDecimal("999.00"), UUID.randomUUID().toString(), null)
        );

        // We calculate balance for A1 only.
        BigDecimal balance = BalanceCalculator.calculateBalance("A1", transactions);

        // Expected: only A1's deposit is counted.
        assertEquals(new BigDecimal("100.00"), balance);
    }

}
