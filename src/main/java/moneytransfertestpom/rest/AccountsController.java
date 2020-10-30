package moneytransfertestpom.rest;

import moneytransfertestpom.account.AccountsService;
import moneytransfertestpom.util.AccountNotFoundException;
import moneytransfertestpom.util.NotEnoughMoneyException;
import org.eclipse.jetty.http.HttpStatus;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import static spark.Spark.*;
import static spark.Spark.exception;

public class AccountsController { private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    private static <T extends Exception> ExceptionHandler<T> handleExceptionWithStatus(int statusCode) {
        return (exception, request, response) -> {
            response.status(statusCode);
            response.body(exception.getMessage());
        };
    }

    public void stopServer() {
        stop();
    }

    public void startServer(int port) {
        port(port);
        post("/accounts", this::createAccount);
        post("/accounts/:id/put-money", this::putMoney);
        post("/accounts/:id/take-money", this::takeMoney);
        post("/transfer", this::transferMoney);
        get("/accounts/:id", this::account);
        notFound("Not found");
        internalServerError("Internal server error");
        exception(AccountNotFoundException.class, handleExceptionWithStatus(HttpStatus.NOT_FOUND_404));
        exception(NotEnoughMoneyException.class, handleExceptionWithStatus(HttpStatus.BAD_REQUEST_400));
        exception(IllegalArgumentException.class, handleExceptionWithStatus(HttpStatus.BAD_REQUEST_400));
    }

    private Integer accountId(Request request) {
        return Integer.valueOf(request.params("id"));
    }

    private String createAccount(Request request, Response response) {
        int id = this.accountsService.createAccount();
        return String.valueOf(id);
    }

    private String account(Request request, Response response) {
        int id = accountId(request);
        int money = this.accountsService.moneyAtAccount(id);
        return String.valueOf(money);
    }

    private Integer intQueryParam(Request request, String param) {
        return Integer.valueOf(request.queryParamOrDefault(param, "0"));
    }

    private String putMoney(Request request, Response response) {
        int id = accountId(request);
        int amount = intQueryParam(request, "amount");
        this.accountsService.putMoneyToAccount(id, amount);
        return "Операция прошла успешно!";
    }

    private String takeMoney(Request request, Response response) {
        int id = accountId(request);
        int amount = intQueryParam(request, "amount");
        this.accountsService.takeMoneyFromAccount(id, amount);
        return "Операция прошла успешно!";
    }

    private String transferMoney(Request request, Response response) {
        int fromId = intQueryParam(request, "from");
        int toId = intQueryParam(request, "to");
        int amount = intQueryParam(request, "amount");
        this.accountsService.transferMoney(fromId, toId, amount);
        return "Деньги переведены!";
    }
}