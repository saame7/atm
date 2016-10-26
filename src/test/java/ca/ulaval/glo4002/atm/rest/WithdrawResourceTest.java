package ca.ulaval.glo4002.atm.rest;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo4002.atm.application.banking.BankingService;
import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionLog;
import org.junit.Before;
import org.junit.Test;

public class WithdrawResourceTest {

    private static final int ACCOUNT_NUMBER = 123;

    private WithdrawResource withdrawResource;
    private BankingService bankingService;
    private WithdrawalRequest withdrawalRequest;

    @Before
    public void setUp() {
        bankingService = mock(BankingService.class);
        withdrawalRequest = new WithdrawalRequest(100);
        withdrawResource = new WithdrawResource(bankingService);
    }

    @Test
    public void withdrawMoneyReturnsTheResultingTransactionLog() {
        TransactionLog expectedTransactionLog = mock(TransactionLog.class);
        willReturn(expectedTransactionLog).given(bankingService).withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        TransactionLog transactionLog = withdrawResource.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        assertSame(expectedTransactionLog, transactionLog);
    }

}
