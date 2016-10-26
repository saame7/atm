package ca.ulaval.glo4002.atm.domain.accounts;

public interface AccountRepository {

    Account findByNumber(int accountNumber);

    void persist(Account account);

}
