import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity(name="reiziger")
public class Reiziger {
    @Id
    @Column(name="reiziger_id")
    private Integer id;
    @Column(name="voorletters")
    private String voorletters;
    @Column(name="tussenvoegsel")
    private String tussenvoegsel;
    @Column(name="achternaam")
    private String achternaam;
    @Column(name="geboortedatum")
    private Date geboortedatum;

    @OneToOne(cascade=CascadeType.ALL, mappedBy = "reiziger", orphanRemoval = true)
    private Adres adres;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reiziger", fetch=FetchType.EAGER)
    private List<OVChipkaart> chipkaarten = new ArrayList<OVChipkaart>();

    public Reiziger() {
    }

    public Reiziger(Integer id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getNaam() {
        if (tussenvoegsel == null || tussenvoegsel.contains("null")) return voorletters.toUpperCase()+". "+achternaam;
        return voorletters.toUpperCase()+". "+tussenvoegsel+" "+achternaam;
    }

    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public void addChipkaart(OVChipkaart chipkaart) {
        chipkaarten.add(chipkaart);
    }

    public void removeChipkaart(OVChipkaart chipkaart) {
        chipkaarten.remove(chipkaart);
    }

    public List<OVChipkaart> getChipkaarten() {
        return chipkaarten;
    }

    public String toString() {
        return "#"+getId().toString()+" "+getNaam()+" ("+getGeboortedatum().toString()+") - Adres { " + ((getAdres() == null ? "Geen" : getAdres().getWoonplaats()+", "+getAdres().getStraat() + " "+getAdres().getHuisnummer()) + " }");
    }

    public void setChipkaarten(List<OVChipkaart> newChipkaarten) {
        this.chipkaarten = newChipkaarten;
    }
}
