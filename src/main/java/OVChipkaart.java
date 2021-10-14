import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name="ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name="kaart_nummer")
    private Integer kaart_nummer;
    @Column(name="geldig_tot")
    private Date geldig_tot;
    @Column(name="klasse")
    private Integer klasse;
    @Column(name="saldo")
    private Float saldo;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="reiziger_id", referencedColumnName = "reiziger_id")
    private Reiziger reiziger;
    @ManyToMany
    @JoinTable(name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "kaart_nummer", referencedColumnName = "kaart_nummer"),
            inverseJoinColumns = @JoinColumn(name="product_nummer", referencedColumnName = "product_nummer")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public OVChipkaart() {
        this.products = new ArrayList<>();
    }

    public OVChipkaart(Integer kaartNummer, Date geldigTotDatum, Integer klasse, Float saldo, Reiziger reiziger) {
        this.kaart_nummer = kaartNummer;
        this.geldig_tot = geldigTotDatum;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.products = new ArrayList<>();
    }

    public Integer getKaartNummer() {
        return kaart_nummer;
    }

    public void setKaartNummer(Integer kaartNummer) {
        this.kaart_nummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldig_tot;
    }

    public void setGeldigTot(Date geldigTotDatum) {
        this.geldig_tot = geldigTotDatum;
    }

    public Integer getKlasse() {
        return klasse;
    }

    public void setKlasse(Integer klasse) {
        this.klasse = klasse;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        return "#"+getKaartNummer().toString()+": geldig tot = "+getGeldigTot().toString()+", klasse = "+getKlasse().toString()+", saldo = "+getSaldo().toString()+" euro, reiziger = {"+getReiziger().getNaam()+"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OVChipkaart chipkaart = (OVChipkaart) o;
        return kaart_nummer.equals(chipkaart.kaart_nummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kaart_nummer);
    }

    public void addProduct(Product newProduct) {
        products.add(newProduct);
        newProduct.ovChipkaarts.add(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.ovChipkaarts.remove(this);
    }
}
