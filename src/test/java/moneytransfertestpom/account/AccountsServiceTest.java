package moneytransfertestpom.account;

import moneytransfertestpom.util.AccountNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountsServiceTest {
    private AccountsService service;

    @Before
    public void initService() {
        this.service = new AccountsService();
    }

    @Test
    public void shouldCreateAccount() {
        int firstId = this.service.createAccount();
        int secondId = this.service.createAccount();
        assertEquals(0, firstId);
        assertEquals(1, secondId);
    }

    @Test
    public void shouldPutMoneyToAccount() {
        int id = this.service.createAccount();
        int amount = 10;
        this.service.putMoneyToAccount(id, amount);
        assertEquals(amount, this.service.moneyAtAccount(id));
    }

    @Test
    public void shouldTakeMoneyFromAccount() {
        int id = this.service.createAccount();
        int putAmount = 10;
        this.service.putMoneyToAccount(id, putAmount);
        int takeAmount = 5;
        this.service.takeMoneyFromAccount(id, takeAmount);
        assertEquals(putAmount - takeAmount, service.moneyAtAccount(id));
    }

    @Test
    public void shouldTransferMoney() {
        int firstId = this.service.createAccount();
        int secondId = this.service.createAccount();
        int firstAmount = 10;
        int transferAmount = 4;
        this.service.putMoneyToAccount(firstId, firstAmount);
        this.service.transferMoney(firstId, secondId, transferAmount);

        assertEquals(firstAmount - transferAmount, service.moneyAtAccount(firstId));
        assertEquals(transferAmount, service.moneyAtAccount(secondId));
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowExceptionOnNotFoundAccount() {
        this.service.moneyAtAccount(52012);
    }
}