package ca.ulaval.glo4002.atm.application.banking;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4002.atm.application.jpa.EntityManagerProvider;
import ca.ulaval.glo4002.atm.application.jpa.EntityManagerProviderForTests;
import ca.ulaval.glo4002.atm.domain.accounts.Account;
import ca.ulaval.glo4002.atm.domain.accounts.AccountNotFoundException;
import ca.ulaval.glo4002.atm.domain.accounts.AccountRepository;
import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionAbortedException;
import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionLog;
import ca.ulaval.glo4002.atm.domain.dispensers.CashDispenser;
import ca.ulaval.glo4002.atm.rest.WithdrawalRequest;
import org.junit.Before;
import org.junit.Test;

public class BankingServiceTest {

    private static final int ACCOUNT_NUMBER = 124;

    private WithdrawalRequest withdrawalRequest;
    private AccountRepository accountRepository;
    private CashDispenser cashDispenser;
    private Account account;
    private TransactionLog transactionLog;

    private BankingService bankingService;

    @Before
    public void setUp() {
        withdrawalRequest = new WithdrawalRequest(80);
        account = mock(Account.class);
        transactionLog = mock(TransactionLog.class);

        accountRepository = mock(AccountRepository.class);
        cashDispenser = mock(CashDispenser.class);
        EntityManagerProvider entityManagerProvider = new EntityManagerProviderForTests();
        bankingService = new BankingService(accountRepository, cashDispenser, entityManagerProvider);

        willReturn(account).given(accountRepository).findByNumber(anyInt());
        willReturn(true).given(cashDispenser).canAccomodateWithdrawal(anyDouble());
        willReturn(transactionLog).given(account).debit(anyDouble());
        willReturn(true).given(transactionLog).isAccepted();
    }

    @Test
    public void withdrawMoneyFindsTheAccountByNumber() {
        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        verify(accountRepository).findByNumber(ACCOUNT_NUMBER);
    }

    @Test(expected = AccountNotFoundException.class)
    public void withdrawMoneySaysAccountNotFoundIfItDoesntExist() {
        willThrow(AccountNotFoundException.class).given(accountRepository).findByNumber(anyInt());

        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);
    }

    @Test
    public void withdrawMoneyValidatesTheWithdrawalRequestWithTheCashDispenser() {
        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        verify(cashDispenser).canAccomodateWithdrawal(withdrawalRequest.amount);
    }

    @Test(expected = TransactionAbortedException.class)
    public void withdrawMoneyAbortsTransactionIfCashDispenserCannotAccomodateRequest() {
        willReturn(false).given(cashDispenser).canAccomodateWithdrawal(anyDouble());

        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);
    }

    @Test
    public void withdrawMoneyDebitsTheAmountFromTheAccount() {
        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        verify(account).debit(withdrawalRequest.amount);
    }

    @Test
    public void withdrawMoneyDispensesTheMoneyIfTheTransactionIsAccepted() {
        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        verify(cashDispenser).dispense(withdrawalRequest.amount);
    }

    public void withdrawMoneyDoesNotDispenseMoneyfTheTransactionLogIsRefused() {
        willReturn(false).given(transactionLog).isAccepted();

        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        verify(cashDispenser, never()).dispense(withdrawalRequest.amount);
    }

    @Test
    public void withdrawMoneyPersistsTheAccount() {
        bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        verify(accountRepository).persist(account);
    }

    @Test
    public void withdrawMoneyReturnsTheGeneratedTransactionLog() {
        TransactionLog expectedTransactionLog = mock(TransactionLog.class);
        willReturn(expectedTransactionLog).given(account).debit(anyDouble());

        TransactionLog transactionLog = bankingService.withdrawMoney(ACCOUNT_NUMBER, withdrawalRequest);

        assertSame(expectedTransactionLog, transactionLog);
    }
}
