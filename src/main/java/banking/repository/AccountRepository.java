package banking.repository;

import banking.domain.account.Account;

public interface AccountRepository {
    void saveAccount(Account account);
    Account findAccount(String accountNumber);
}
