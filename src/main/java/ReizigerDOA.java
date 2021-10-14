import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;

public interface ReizigerDOA {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);

    Reiziger findById(Integer id) throws NoResultException;
    List<Reiziger> findByGbdatum(String datum);
    List<Reiziger> findAll();
}
