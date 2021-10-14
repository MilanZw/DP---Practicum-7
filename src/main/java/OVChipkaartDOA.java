import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OVChipkaartDOA {
    boolean save(OVChipkaart chipkaart);
    boolean update(OVChipkaart chipkaart);
    boolean delete(OVChipkaart chipkaart);
    
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findAll() throws SQLException;
}