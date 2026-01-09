package banking.domain.account;
import banking.domain.transaction.TransactionType;
import banking.exception.InvalidAccountStateException;
import banking.exception.InvalidStateTransitionException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {
    @ParameterizedTest
    @CsvSource({
            "CLOSED, ACTIVE",
            "CLOSED, PENDING",
            "CLOSED, FROZEN",
            "ACTIVE, PENDING",
            "PENDING, FROZEN",
    })
    void invalidStateTransitions_throwsInvalidStateTransitionException(
            AccountState fromState,
            AccountState toState
    ) {
        Account account = new Account("1234", fromState);

        assertThrows(InvalidStateTransitionException.class, () -> account.changeAccountState(toState));
    }

    @ParameterizedTest
    @CsvSource({
            "CLOSED, DEPOSIT",
            "CLOSED, WITHDRAW",
            "CLOSED, TRANSFER_DEBIT",
            "CLOSED, TRANSFER_CREDIT",
            "FROZEN, WITHDRAW",
            "PENDING, WITHDRAW"
    })
    void invalidAccountState_ThrowsInvalidAccountStateTransition(AccountState state, TransactionType transactionType) {
        Account account = new Account(UUID.randomUUID().toString(), state);

        assertThrows(InvalidAccountStateException.class, () -> {
            switch (transactionType) {
                case DEPOSIT -> account.assertCanDeposit();
                case WITHDRAW -> account.assertCanWithdraw();
                case TRANSFER_DEBIT -> account.assertCanTransfer();
                case TRANSFER_CREDIT -> account.assertCanTransfer();
            }
        });
    }
}

