import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.metamodel.EntityType;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;
    private static final ReizigerDOA rdao;
    private static final OVChipkaartDOA cdao;
    private static final ProductDOA pdao;
    private static final AdresDOA adao;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
            // Create DOAs
            rdao = new ReizigerDOAHibernate();
            cdao = new OVChipkaartDOAHibernate();
            pdao = new ProductDOAHibernate();
            adao = new AdresDOAHibernate();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void main(String[] args) throws SQLException {
        HibernateUtil.openSession(factory);
        testFetchAll();
        testDAOHibernate();
        HibernateUtil.closeSession();
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = HibernateUtil.startTransaction();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        }
        finally {
            HibernateUtil.endTransaction();
        }
    }

    private static void testDAOHibernate() throws SQLException {
        // Run all the tests from the prior assignments
        testReizigerDAO();
        testAdresDAO();
        testChipkaartDAO();
        testProductDAO();
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO() throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Verwijder sietske als hij al bestaat
        try {
            Reiziger existSietske = rdao.findById(77);
            rdao.delete(existSietske);
        }
        catch (NoResultException exc) {
            System.out.println("[Test] Sietske bestaat nog niet, dus de vorige test resultaat was succesvol!");
        }

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal de gebruiker op via zijn geboortedatum
        for (Reiziger reiz : rdao.findByGbdatum("1981-03-14")) {
            System.out.println("[Test] Reiziger '"+reiz.toString()+"' gevonden met 1981-03-14 als geboortedatum!");
        }

        // Update de geboortedatum van deze gebruiker
        sietske.setGeboortedatum(java.sql.Date.valueOf("1982-04-15"));
        if (rdao.update(sietske)) System.out.println("[Test] Het veranderen van de geboortedatum naar 1982-04-15 was succesvol!");
        else System.out.println("[Test] Het veranderen van de geboortedatum was niet succesvol!");

        // Probeer de gebruiker opnieuw op te halen via zijn geboortedatum
        for (Reiziger reiz : rdao.findByGbdatum("1982-04-15")) {
            System.out.println("[Test] Reiziger '"+reiz.toString()+"' is weer gevonden met 1982-04-15 als geboortedatum!");
        }

        // Verwijder gebruiker weer
        if (rdao.delete(sietske)) System.out.println("[Test] Sietske is weer succesvol verwijderd!");
        else System.out.println("[Test] Sietske kon niet succesvol worden verwijderd!");

        // Haal nummer van gebruikers weer op
        System.out.println("[Test] Er zijn nu weer "+String.valueOf(rdao.findAll().size())+" reizigers!");
    }

    /**
     * P3. Adres DAO: Persistentie van twee klassen met een één-op-één-relatie
     *
     * Deze methode test de CRUD-functionaliteit van de Adres DAO
     *
     * @throws SQLException
     */
    private static void testAdresDAO() throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        List<Adres> adressen = adao.findAll();
        for (Adres adr : adressen) {
            System.out.println(adr.toString());
        }
        System.out.println();

        // Verwijder adres
        System.out.println("[Test] We hebben "+String.valueOf(adressen.size())+" adressen momenteel, voordat we er ééntje gaan verwijderen!");
        Adres deleteAdres = adressen.get(0);
        if (!adao.delete(deleteAdres)) System.out.println("[Test] Kon adres niet verwijderen!");
        else System.out.println("[Test] Adres succesvol verwijderd!");

        // Maak nieuw adres en voeg het aan dezelfde persoon toe
        Reiziger adresParent = deleteAdres.getReiziger();
        Random rd = new Random();
        Integer newReizigerId = rd.nextInt(200) + 20;
        Integer newAdresId = rd.nextInt(200) + 20;

        String gbDatum = "1981-03-14";
        Reiziger newTestUser = new Reiziger(newReizigerId, "Test"+newReizigerId, "", "Gebruiker", java.sql.Date.valueOf(gbDatum));
        rdao.save(newTestUser);

        Adres newAdr = new Adres(newAdresId, "3916AE", "1a", "Straatweg", "Apeldoorn", newTestUser);
        adao.save(newAdr);
        // Update de reiziger met het nieuwe adres
        adresParent.setAdres(newAdr);

        if (!rdao.update(adresParent)) System.out.println("[Test] Iets ging fout tijdens het opslaan van het nieuwe adres in de reiziger!");  // Dit zou ook het adres moeten updaten/toevoegen
        else System.out.println("[Test] Succesvol het nieuwe adres geupdated/toegevoegd aan de reiziger.");

        // Update adres met nieuw huisnummer
        System.out.println("[Test] Huidige adres = "+adresParent.getAdres().toString());
        adresParent.getAdres().setHuisnummer("4");
        if (!adao.update(adresParent.getAdres())) System.out.println("[Test] Iets ging fout tijdens het updaten van huisnummer!"); // Update het adres via de adao
        System.out.println("[Test] Bijgewerkte adres = "+adresParent.getAdres().toString());
    }

    /**
     * P4. Chipkaart DAO: Persistentie van twee klassen met een één-op-veel-relatie
     *
     * Deze methode test de CRUD-functionaliteit van de Chipkaart DAO
     *
     * @throws SQLException
     */
    private static void testChipkaartDAO() throws SQLException {
        System.out.println("\n---------- Test ChipkaartDAO -------------");

        // Haal alle chipkaarten op uit de database
        System.out.println("[Test] ChipkaartDAO.findAll() geeft de volgende chipkaarten:");
        List<OVChipkaart> chipkaarten = cdao.findAll();
        for (OVChipkaart chip : chipkaarten) {
            System.out.println(chip.toString());
        }
        System.out.println();

        // Krijg de eerste chipkaart en de eigenaar
        OVChipkaart deleteChipkaart = chipkaarten.get(0);
        Reiziger parentReiziger = deleteChipkaart.getReiziger();
        System.out.println("[Test] "+parentReiziger.getNaam()+" heeft "+String.valueOf(parentReiziger.getChipkaarten().size())+" chipkaarten!");
        if (!cdao.delete(deleteChipkaart)) System.out.println("[Test] Niet gelukt om de chipkaart te verwijderen!");
        else System.out.println("[Test] Één chipkaart is succesvol verwijderd!");

        // Voeg een nieuwe chipkaart toe
        Reiziger parentAddReiziger = rdao.findAll().get(0);
        OVChipkaart newChip = new OVChipkaart(deleteChipkaart.getKaartNummer(), Date.valueOf("2020-10-31"), 2, 30.0f, parentAddReiziger);
        parentAddReiziger.addChipkaart(newChip);
        cdao.save(newChip);
        if (!rdao.update(parentAddReiziger)) System.out.println("[Test] Gefaald om het parentAddReiziger te updaten met het nieuwe adres dat was toegevoegd!");
        else System.out.println("[Test] Succesvol een nieuwe chipkaart gemaakt voor "+parentAddReiziger.getNaam()+"!");

        List<OVChipkaart> reizResults = cdao.findByReiziger(parentAddReiziger);
        OVChipkaart sameChip = null;
        for (OVChipkaart chip : reizResults) {
            if (chip.getKaartNummer().equals(newChip.getKaartNummer())) sameChip = chip;
        }

        if (sameChip == null) System.out.println("[Test] findByReiziger() kon niet de net toegevoegde chip vinden!");
        else {
            System.out.println("[Test] Kon succesvol de net toegevoegde chip vinden via findByReiziger()!");

            sameChip.setKlasse(1);
            if (!cdao.update(sameChip)) System.out.println("[Test] Gefaald om de klasse te veranderen van 2 naar 1!");
            else System.out.println("[Test] Succesvol de klasse veranderd van 2 naar 1!");
        }
    }

    /**
     * P5. Product DAO: Persistentie van twee klassen met een veel-op-veel-relatie
     *
     * Deze methode test de CRUD-functionaliteit van de Product DAO
     *
     * @throws SQLException
     */
    private static void testProductDAO() throws SQLException, RuntimeException {
        System.out.println("\n---------- Test ProductDAO -------------");
        // Haal alle producten op uit de database
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        List<Product> products = pdao.findAll();
        for (Product prod : products) {
            System.out.println(prod.toString());
        }
        System.out.println();

        Random rd = new Random();
        Integer testProductId = rd.nextInt(400)+100;
        Integer testChipkaartId = rd.nextInt(400)+100;
        Product newProduct = new Product(testProductId, "Test Product #"+testProductId, "Dit product is hier om te testen!", 20.0, null, null);

        // Maak een nieuw product aan
        if (!pdao.save(newProduct))
            throw new RuntimeException("[Test] Gefaald om een nieuw product toe te voegen!");

        // Check of het nieuwe product is aangemaakt
        if (pdao.findAll().size() != products.size()+1)
            System.out.println("[Test] Succesvol een nieuw product aangemaakt!");

        // Voeg een nieuwe chipkaart toe
        Reiziger parentAddOv = rdao.findAll().get(0);
        OVChipkaart newChip = new OVChipkaart(testChipkaartId, Date.valueOf("2020-10-31"), 2, 30.0f, parentAddOv);
        parentAddOv.addChipkaart(newChip);
        if (!cdao.save(newChip)) System.out.println("[Test] Gefaald om de nieuwe ovchipkaart toe te voegen!");
        else System.out.println("[Test] Succesvol een nieuwe test chipkaart gemaakt voor "+parentAddOv.getNaam()+" om de producten aan toe te voegen!");

        newChip.addProduct(newProduct);
        if (!cdao.update(newChip))
            throw new RuntimeException("[Test] Gefaald met het toevoegen van het product!");
        System.out.println("[Test] Het product is succesvol toegevoegd aan de nieuwe chipkaart!");

        // Probeer de chipkaart te vinden via het product
        if (pdao.findByOVChipkaart(newChip).size() == 0)
            throw new RuntimeException("[Test] Gefaald met het ophalen van de chipkaart via het product.");

        // Bewerk bestaand product
        Product editProduct = pdao.findByOVChipkaart(newChip).get(0);
        editProduct.setNaam("Nieuwe Naam");
        if (!pdao.update(editProduct))
            throw new RuntimeException("[Test] Kon het product niet bewerken.");

        if (pdao.findByOVChipkaart(newChip).get(0).getNaam().equals("Nieuwe Naam"))
            System.out.println("[Test] Product is succesvol bewerkt!");

        // Verwijder de test chipkaart en daarbij ook alle producten die de chipkaart had
        if (!cdao.delete(newChip))
            throw new RuntimeException("[Test] Gefaald met het verwijderen van de chipkaart en daarmee ook de relaties naar het product!");
        System.out.println("[Test] De chipkaart is weer succesvol verwijderd!");

        // Verwijder het product direct en ook alle relaties
        if (!pdao.delete(newProduct))
            throw new RuntimeException("[Test] Gefaald met het verwijderen van het test product!");
        System.out.println("[Test] Het test product is weer verwijderd!");
    }
}