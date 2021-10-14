import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDOAHibernate implements ProductDOA {
    @Override
    public boolean save(Product product) {
        Session session = HibernateUtil.startTransaction();
        session.save(product);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean update(Product product) {
        Session session = HibernateUtil.startTransaction();
        session.update(product);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean delete(Product product) {
        Session session = HibernateUtil.startTransaction();
        session.delete(product);
        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart chipkaart) {
        Session session = HibernateUtil.startTransaction();

        ArrayList<Product> filteredProductsList = new ArrayList<>();
        for (Product prod : session.createQuery("from product", Product.class).list()) {
            for (OVChipkaart chip : prod.getChipkaarts()) {
                if (chip.equals(chipkaart)) {
                    filteredProductsList.add(prod);
                }
            }
        }

        HibernateUtil.endTransaction();
        return filteredProductsList;
    }

    @Override
    public List<Product> findAll() {
        Session session = HibernateUtil.startTransaction();

        List<Product> products = session.createQuery("from product", Product.class).list();

        HibernateUtil.endTransaction();
        return products;
    }

    @Override
    public boolean deleteProductFromAllChipkaarts(Product product) {
        Session session = HibernateUtil.startTransaction();

        Query deleteProductsFromAllQuery = session.createSQLQuery("delete from ov_chipkaart_product as op where op.product_nummer = :product_nummer");
        deleteProductsFromAllQuery.setParameter("product_nummer", product.getId());
        deleteProductsFromAllQuery.executeUpdate();

        HibernateUtil.endTransaction();
        return true;
    }

    @Override
    public boolean deleteByOVChipkaart(OVChipkaart chipkaart) {
        Session session = HibernateUtil.startTransaction();

        Query deleteProductsFromChipkaartQuery = session.createSQLQuery("delete from ov_chipkaart_product as op where op.kaart_nummer = :kaart_nummer");
        deleteProductsFromChipkaartQuery.setParameter("kaart_nummer", chipkaart.getKaartNummer());
        deleteProductsFromChipkaartQuery.executeUpdate();

        HibernateUtil.endTransaction();
        return true;
    }
}
