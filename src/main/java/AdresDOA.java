import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface AdresDOA {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);

    Adres findByReiziger(Reiziger reiziger);
    Adres findById(Integer id);
    List<Adres> findAll();
}
