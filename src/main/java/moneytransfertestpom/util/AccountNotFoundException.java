package moneytransfertestpom.util;

public class AccountNotFoundException  extends RuntimeException {
    public AccountNotFoundException(int accountId) {
        super("Учетная запись не найдена с идентификатором " + accountId);
    }
}
