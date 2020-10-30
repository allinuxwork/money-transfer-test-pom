package moneytransfertestpom.account;

import moneytransfertestpom.util.NotEnoughMoneyException;

public class Account {
    private final int id;

    private int money;

    public Account(int id) {
        this.id = id;
        this.money = 0;
    }

    public void put(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Cумма денег отрицательна!");
        }
        this.money += amount;
    }

    public void take(int amount) {
        if (!available(amount)) {
            throw new NotEnoughMoneyException(this);
        }
        this.money -= amount;
    }

    public boolean available(int amount) {
        return amount <= this.money;
    }

    public int getId() {
        return this.id;
    }

    public int getMoney() {
        return this.money;
    }
}