package moneytransfertestpom.account;

import moneytransfertestpom.util.AccountNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AccountsService {
    private final List<Account> accounts = new ArrayList<>();

    public int createAccount() {
        synchronized (this.accounts) {
            int nextId = this.accounts.size();
            Account account = new Account(nextId);
            this.accounts.add(account);
            return nextId;
        }
    }

    public void putMoneyToAccount(int accountId, int amount) {
        Account account = accountOf(accountId);
        synchronized (account) {
            account.put(amount);
        }
    }

    public void takeMoneyFromAccount(int accountId, int amount) {
        Account account = accountOf(accountId);
        synchronized (account) {
            account.take(amount);
        }
    }

    public void transferMoney(int accountIdFrom, int accountIdTo, int amount) {
        if (accountIdFrom == accountIdTo) {
            return;
        }
        Account accountFrom = accountOf(accountIdFrom);
        Account accountTo = accountOf(accountIdTo);
        syncOnAccountsPair(accountFrom, accountTo, () -> {
            accountFrom.take(amount);
            accountTo.put(amount);
        });
    }

    public int moneyAtAccount(int accountId) {
        return accountOf(accountId).getMoney();
    }

    private Account accountOf(int accountId) {
        if (accountId >= this.accounts.size() || accountId < 0) {
            throw new AccountNotFoundException(accountId);
        }
        return this.accounts.get(accountId);
    }

    private void syncOnAccountsPair(Account left, Account right, Runnable runnable) {
        Account first;
        Account second;
        if (left.getId() < right.getId()) {
            first = left;
            second = right;
        } else {
            first = right;
            second = left;
        }

        synchronized (first) {
            synchronized (second) {
                runnable.run();
            }
        }
    }
}