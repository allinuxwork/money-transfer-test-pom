package moneytransfertestpom;

import moneytransfertestpom.account.AccountsService;
import moneytransfertestpom.rest.AccountsController;

public class MoneyTransferApplication {
    public static void main(String[] args) {
        AccountsService accountsService = new AccountsService();
        AccountsController accountsController = new AccountsController(accountsService);
        accountsController.startServer(8081);
    }
}