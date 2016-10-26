package ca.ulaval.glo4002.atm.application.jpa;

import javax.persistence.EntityManager;

public class EntityManagerProviderForTests extends EntityManagerProvider {

    @Override
    public void executeInTransaction(Runnable transaction) {
        transaction.run();
    }

    @Override
    public EntityManager getEntityManager() {
        throw new RuntimeException("Unit tests should not use the entity manager");
    }

}
