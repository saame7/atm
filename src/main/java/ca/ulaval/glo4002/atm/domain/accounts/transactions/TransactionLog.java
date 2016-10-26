package ca.ulaval.glo4002.atm.domain.accounts.transactions;

public class TransactionLog {

    public static TransactionLog accepted(double amount) {
        return new TransactionLog(true, amount);
    }

    public static TransactionLog refused(double amount) {
        return new TransactionLog(false, amount);
    }

    private boolean accepted;
    private double amount;

    private TransactionLog(boolean accepted, double amount) {
        this.accepted = accepted;
        this.amount = amount;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public double getAmount() {
        return amount;
    }

}
