import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDOAHibernate implements AdresDOA {
    @Override
    public boolean save(Adres adres) {
        Session session = HibernateUtil.startTransaction();
        session.save(adres);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean update(Adres adres) {
        Session session = HibernateUtil.startTransaction();
        session.update(adres);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean delete(Adres adres) {
        Session deleteRelationSession = HibernateUtil.startTransaction();
        Query deleteRelationQuery = deleteRelationSession.createSQLQuery("UPDATE reiziger AS r SET adres_id = null WHERE r.reiziger_id = :id");
        deleteRelationQuery.setParameter("id", adres.getReiziger().getId());
        deleteRelationQuery.executeUpdate();
        HibernateUtil.endTransaction();

        Session session = HibernateUtil.startTransaction();
        Query deleteRowQuery = session.createQuery("delete from adres as a where a.id = :id");
        deleteRowQuery.setParameter("id", adres.getId());
        System.out.println(deleteRowQuery.executeUpdate());
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Session session = HibernateUtil.startTransaction();

        Query<Adres> adresQuery = session.createQuery("from adres as a where a.reiziger.id = :id", Adres.class);
        adresQuery.setParameter("id", reiziger.getId());
        Adres adres = adresQuery.getSingleResult();

        HibernateUtil.endTransaction();
        return adres;
    }

    @Override
    public Adres findById(Integer id) {
        Session session = HibernateUtil.startTransaction();

        Query<Adres> adresQuery = session.createQuery("from adres as a where a.id = :id", Adres.class);
        adresQuery.setParameter("id", id);
        Adres adres = adresQuery.getSingleResult();

        HibernateUtil.endTransaction();
        return adres;
    }

    @Override
    public List<Adres> findAll() {
        Session session = HibernateUtil.startTransaction();

        List<Adres> adressen = session.createQuery("from adres", Adres.class).list();

        HibernateUtil.endTransaction();
        return adressen;
    }
}
