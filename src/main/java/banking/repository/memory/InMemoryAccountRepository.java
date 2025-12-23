package banking.repository.memory;

import banking.domain.account.Account;
import banking.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts;

    public InMemoryAccountRepository() {
        accounts = new HashMap<>();
    }

    @Override
    public void saveAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}
