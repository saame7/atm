package ca.ulaval.glo4002.atm.domain.accounts;

import javax.persistence.Entity;

import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionLog;

@Entity
public class StandardAccount extends Account {

    protected StandardAccount() {
        super(); // for hibernate
    }

    public StandardAccount(int accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public TransactionLog debit(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return TransactionLog.accepted(amount);
        }

        return TransactionLog.refused(amount);
    }

    public void credit(double amount) {
        balance += amount;
    }

}
