package ca.ulaval.glo4002.atm.domain.dispensers;

public interface CashDispenser {

    boolean canAccomodateWithdrawal(double amount);

    void dispense(double amount);

}
