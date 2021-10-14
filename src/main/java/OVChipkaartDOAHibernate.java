import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDOAHibernate implements OVChipkaartDOA {
    @Override
    public boolean save(OVChipkaart chipkaart) {
        Session session = HibernateUtil.startTransaction();
        session.save(chipkaart);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean update(OVChipkaart chipkaart) {
        Session updateChipSession = HibernateUtil.startTransaction();
        updateChipSession.update(chipkaart);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean delete(OVChipkaart chipkaart) {
        Session session = HibernateUtil.startTransaction();
        session.delete(chipkaart);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        Session session = HibernateUtil.startTransaction();

        Query<OVChipkaart> productsQuery = session.createQuery("from ov_chipkaart as o where o.reiziger.id = :id", OVChipkaart.class);
        productsQuery.setParameter("id", reiziger.getId());
        List<OVChipkaart> products = productsQuery.list();

        HibernateUtil.endTransaction();
        return products;
    }

    @Override
    public List<OVChipkaart> findAll() {
        Session session = HibernateUtil.startTransaction();

        List<OVChipkaart> products = session.createQuery("from ov_chipkaart", OVChipkaart.class).list();

        HibernateUtil.endTransaction();
        return products;
    }
}
