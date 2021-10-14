import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {
    private static SessionFactory factory;
    private static Session activeSession;

    public static void openSession(SessionFactory passFactory) {
        factory = passFactory;
        //activeSession = factory.openSession();
    }

    public static void closeSession() {
        //activeSession.close();
    }

    public static Session getSession() {
        return factory.openSession();
    }

    public static Session startTransaction() {
        activeSession = factory.openSession();
        activeSession.beginTransaction();
        return activeSession;
    }

    public static void endTransaction() {
        activeSession.getTransaction().commit();
        activeSession.close();
    }
}
