package banking.domain.account;
import banking.exception.InvalidAccountStateException;
import banking.exception.InvalidStateTransitionException;



import java.time.Instant;

public class Account {
    private final String accountNumber;
    private final Instant createdAt;
    private AccountState accountState;

    public Account(String accountNumber, AccountState accountState) {
        this.accountNumber = accountNumber;
        this.createdAt = Instant.now();
        this.accountState = accountState;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void changeAccountState(AccountState state) {
        if (accountState == AccountState.CLOSED) {
            throw new InvalidStateTransitionException(
                    "Cannot change state from CLOSED to " + state
            );
        }

        if (accountState == AccountState.ACTIVE && state == AccountState.PENDING) {
            throw new InvalidStateTransitionException(
                    "Cannot change state from " + accountState + " to " + state
            );
        }

        if (accountState == AccountState.PENDING && state == AccountState.FROZEN) {
            throw new InvalidStateTransitionException(
                    "Cannot change state from " + accountState + " to " + state
            );
        }

        this.accountState = state;
    }

    public void assertCanDeposit() {
        if (accountState != AccountState.ACTIVE
                && accountState != AccountState.FROZEN
                && accountState != AccountState.PENDING) {

            throw new InvalidAccountStateException("Deposits not allowed when account state is " + accountState);
        }
    }

    public void assertCanWithdraw() {
        if (accountState != AccountState.ACTIVE) {
            throw new InvalidAccountStateException("Withdrawals not allowed when account state is " + accountState);
        }
    }

    public void assertCanTransfer() {
        if (accountState != AccountState.ACTIVE) {
            throw new InvalidAccountStateException("Transfers not allowed when account state is " + accountState);
        }
    }

}
