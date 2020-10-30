package moneytransfertestpom.account;

import moneytransfertestpom.util.NotEnoughMoneyException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class AccountTest {
    @Test
    public void shouldCreateAccountWithZeroMoney() {
        Account account = new Account(1);
        assertEquals("should be no money", 0, account.getMoney());
    }

    @Test
    public void shouldPutMoneyToAccount() {
        int amount = 12;
        Account account = new Account(1);
        account.put(amount);
        assertEquals("should put money", amount, account.getMoney());
    }

    @Test
    public void shouldCheckAmountAvailability() {
        int putAmount = 10;
        int availableAmount = 5;
        int unavailableAmount = 15;
        Account account = new Account(1);
        account.put(putAmount);
        assertFalse("unavailable money", account.available(unavailableAmount));
        assertTrue("available money", account.available(availableAmount));
    }

    @Test
    public void shouldTakeMoneyFromAccount() {
        int putAmount = 12;
        int takeAmount = 10;
        Account account = new Account(1);
        account.put(putAmount);
        account.take(takeAmount);

        assertEquals("should take money", putAmount - takeAmount, account.getMoney());
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void shouldThrowExceptionOnNotEnoughMoney() {
        int putAmount = 12;
        int takeAmount = 100;
        Account account = new Account(1);
        account.put(putAmount);
        account.take(takeAmount);
    }
}