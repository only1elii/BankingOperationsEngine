import banking.domain.account.Account;
import banking.domain.account.AccountState;
import banking.domain.transaction.TransactionType;
import banking.exception.InvalidAccountStateException;
import banking.exception.InvalidStateTransitionException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
            "FROZEN, DEPOSIT",
            "PENDING, WITHDRAW",
            "PENDING, TRANSFER",
    })

    void invalidAccountState_ThrowsIvalidAccountStateTransition(
            AccountState accountState, TransactionType transactionType
    ) {
        Account account = new Account("1234", accountState);


    }
}
