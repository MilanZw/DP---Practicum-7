import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ProductDOA {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);

    List<Product> findByOVChipkaart(OVChipkaart chipkaart);
    List<Product> findAll();
    boolean deleteProductFromAllChipkaarts(Product product);
    boolean deleteByOVChipkaart(OVChipkaart chipkaart);
}
