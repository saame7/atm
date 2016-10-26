package ca.ulaval.glo4002.atm.infrastructure.dispensers;

import ca.ulaval.glo4002.atm.domain.dispensers.CashDispenser;

public class FakeCashDispenser implements CashDispenser {

    @Override
    public boolean canAccomodateWithdrawal(double amount) {
        return true;
    }

    @Override
    public void dispense(double amount) {
        System.out.println("Dispensed money : " + amount);
    }

}
