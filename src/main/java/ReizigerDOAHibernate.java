import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.List;

public class ReizigerDOAHibernate implements ReizigerDOA {
    @Override
    public boolean save(Reiziger reiziger) {
        Session session = HibernateUtil.startTransaction();
        session.save(reiziger);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Session session = HibernateUtil.startTransaction();
        session.update(reiziger);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        Session session = HibernateUtil.startTransaction();
        session.delete(reiziger);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public Reiziger findById(Integer id) throws NoResultException {
        Session session = HibernateUtil.startTransaction();

        Query<Reiziger> reizigerQuery = session.createQuery("from reiziger as r where r.id = :id", Reiziger.class);
        reizigerQuery.setParameter("id", id);
        Reiziger reizigers = reizigerQuery.getSingleResult();

        HibernateUtil.endTransaction();
        return reizigers;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        Session session = HibernateUtil.startTransaction();

        Query<Reiziger> reizigersQuery = session.createQuery("from reiziger as r where r.geboortedatum = :date", Reiziger.class);
        reizigersQuery.setParameter("date", Date.valueOf(datum));
        List<Reiziger> reizigers = reizigersQuery.list();

        HibernateUtil.endTransaction();
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        Session session = HibernateUtil.startTransaction();

        List<Reiziger> reizigers = session.createQuery("from reiziger", Reiziger.class).list();

        HibernateUtil.endTransaction();
        return reizigers;
    }
}
