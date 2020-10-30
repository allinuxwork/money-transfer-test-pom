package moneytransfertestpom.util;

import moneytransfertestpom.account.Account;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(Account account) {
        super("Account " + account.getId() + " не хватает денег.");
    }
}
