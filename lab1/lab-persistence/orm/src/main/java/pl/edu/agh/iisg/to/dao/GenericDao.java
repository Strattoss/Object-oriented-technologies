package pl.edu.agh.iisg.to.dao;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.iisg.to.session.SessionService;

public abstract class GenericDao<T> {

    public T save(final T object) throws PersistenceException {
        final Session session = SessionService.getSession();
        final Transaction tx = session.beginTransaction();
        session.save(object);
        session.merge(object);
        tx.commit();
        return object;
    }

    public void update(final T object) throws PersistenceException {
        final Session session = SessionService.getSession();
        final Transaction tx = session.beginTransaction();
        session.update(object);
        session.merge(object);
        tx.commit();
    }

    public Session currentSession() {
        return SessionService.getSession();
    }
}
