package moneytransfertestpom.account;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class AccountsServiceLoadTest {

    private static final int COUNT = 10000;

    private static final int AMOUNT = 10;

    private static final int TOTAL = COUNT * AMOUNT;

    private AccountsService service;

    @Before
    public void initService() {
        this.service = new AccountsService();
    }

    @Test
    public void manyMoneyPut() {
        int id = this.service.createAccount();
        runAsyncManyTimes(() -> this.service.putMoneyToAccount(id, AMOUNT));
        assertEquals(TOTAL, this.service.moneyAtAccount(id));
    }

    @Test
    public void manyMoneyTake() {
        int id = this.service.createAccount();
        this.service.putMoneyToAccount(id, AMOUNT * COUNT);
        runAsyncManyTimes(() -> this.service.takeMoneyFromAccount(id, AMOUNT));
        assertEquals(0, this.service.moneyAtAccount(id));
    }

    @Test
    public void manyMoneyTransfer() {
        int first = this.service.createAccount();
        int second = this.service.createAccount();
        this.service.putMoneyToAccount(first, TOTAL);
        this.service.putMoneyToAccount(second, TOTAL);
        runAsyncManyTimes(
                () -> this.service.transferMoney(first, second, AMOUNT),
                () -> this.service.transferMoney(second, first, AMOUNT));

        assertEquals(TOTAL, this.service.moneyAtAccount(first));
        assertEquals(TOTAL, this.service.moneyAtAccount(second));
    }

    private void runAsyncManyTimes(Runnable... runnables) {
        List<CompletableFuture<Void>> futures = new ArrayList<>(COUNT * runnables.length);
        for (int i = 0; i < COUNT; i++) {
            for (Runnable runnable : runnables) {
                futures.add(CompletableFuture.runAsync(runnable));
            }
        }
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
            }
        });
    }

}