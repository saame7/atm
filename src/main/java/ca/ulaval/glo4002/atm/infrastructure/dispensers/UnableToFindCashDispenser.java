package ca.ulaval.glo4002.atm.infrastructure.dispensers;

public class UnableToFindCashDispenser extends RuntimeException {

    public UnableToFindCashDispenser() {
    }

    public UnableToFindCashDispenser(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 1L;

}
