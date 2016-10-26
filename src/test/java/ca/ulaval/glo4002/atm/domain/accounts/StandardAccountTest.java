package ca.ulaval.glo4002.atm.domain.accounts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionLog;
import org.junit.Before;
import org.junit.Test;

public class StandardAccountTest {

    private StandardAccount account;

    @Before
    public void setUp() {
        account = new StandardAccount();
    }

    @Test
    public void debitCreatesAnAcceptedTransactionLogIfAccountHasEnoughFunds() {
        double amountInAccount = 500;
        account.credit(amountInAccount);

        TransactionLog transactionLog = account.debit(amountInAccount);

        assertTrue(transactionLog.isAccepted());
    }

    @Test
    public void debitRemovesTheAmountFromTheAccount() {
        double amountInAccount = 500;
        account.credit(amountInAccount);

        account.debit(amountInAccount);
        TransactionLog transactionLog = account.debit(amountInAccount);

        assertFalse(transactionLog.isAccepted());
    }

    @Test
    public void debitCreatesARefusedTransactionLogIAccountDoesNotHaveEnoughFunds() {
        double amountInAccount = 500;
        account.credit(amountInAccount);

        TransactionLog transactionLog = account.debit(amountInAccount + 1);

        assertFalse(transactionLog.isAccepted());
    }

}
