package ca.ulaval.glo4002.atm.application.banking;

import ca.ulaval.glo4002.atm.application.ServiceLocator;
import ca.ulaval.glo4002.atm.application.jpa.EntityManagerProvider;
import ca.ulaval.glo4002.atm.domain.accounts.Account;
import ca.ulaval.glo4002.atm.domain.accounts.AccountRepository;
import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionAbortedException;
import ca.ulaval.glo4002.atm.domain.accounts.transactions.TransactionLog;
import ca.ulaval.glo4002.atm.domain.dispensers.CashDispenser;
import ca.ulaval.glo4002.atm.rest.WithdrawalRequest;

public class BankingService {

    private AccountRepository accountRepository;
    private CashDispenser cashDispenser;
    private EntityManagerProvider entityManagerProvider;

    public BankingService() {
        this.accountRepository = ServiceLocator.resolve(AccountRepository.class);
        this.cashDispenser = ServiceLocator.resolve(CashDispenser.class);
        this.entityManagerProvider = new EntityManagerProvider();
    }

    public BankingService(AccountRepository accountRepository, CashDispenser cashDispenser, EntityManagerProvider provider) {
        this.accountRepository = accountRepository;
        this.cashDispenser = cashDispenser;
        this.entityManagerProvider = provider;
    }

    public TransactionLog withdrawMoney(int accountNumber, WithdrawalRequest withdrawalRequest) {
        if (!cashDispenser.canAccomodateWithdrawal(withdrawalRequest.amount)) {
            throw new TransactionAbortedException();
        }

        TransactionLog transactionLog = executeTransaction(accountNumber, withdrawalRequest);

        if (transactionLog.isAccepted()) {
            cashDispenser.dispense(withdrawalRequest.amount);
        }

        return transactionLog;
    }

    private TransactionLog executeTransaction(int accountNumber, WithdrawalRequest withdrawalRequest) {
        Account account = accountRepository.findByNumber(accountNumber);
        TransactionLog transactionLog = account.debit(withdrawalRequest.amount);

        entityManagerProvider.executeInTransaction(() -> accountRepository.persist(account));

        return transactionLog;
    }
}
