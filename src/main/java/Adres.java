import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name="adres")
public class Adres implements Serializable {
    @Id
    @Column(name="adres_id")
    private Integer id;

    @Column(name="postcode")
    private String postcode;
    @Column(name="huisnummer")
    private String huisnummer;
    @Column(name="straat")
    private String straat;
    @Column(name="woonplaats")
    private String woonplaats;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="reiziger_id", referencedColumnName = "reiziger_id")
    private Reiziger reiziger;

    public Adres() {
    }

    public Adres(Integer id, String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public String toString() {
        return "#"+getId().toString()+":"+getStraat()+" "+getHuisnummer()+", "+getWoonplaats()+", "+getPostcode()+" - Reiziger { "+(reiziger != null ? reiziger.getNaam() : "None")+" }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adres adres = (Adres) o;
        return id.equals(adres.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
