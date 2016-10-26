package ca.ulaval.glo4002.atm.application.jpa;

import javax.persistence.EntityManager;

public class EntityManagerProvider {

    private static ThreadLocal<EntityManager> localEntityManager = new ThreadLocal<>();

    public EntityManager getEntityManager() {
        return localEntityManager.get();
    }

    public void executeInTransaction(Runnable transaction) {
        localEntityManager.get().getTransaction().begin();
        transaction.run();
        localEntityManager.get().getTransaction().commit();
    }

    public static void setEntityManager(EntityManager entityManager) {
        localEntityManager.set(entityManager);
    }

    public static void clearEntityManager() {
        localEntityManager.set(null);
    }
}
