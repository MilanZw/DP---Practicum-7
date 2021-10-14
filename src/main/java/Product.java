import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name="product")
public class Product {
    @Id
    @Column(name="product_nummer")
    private Integer id;
    @Column(name="naam")
    private String naam;
    @Column(name="beschrijving")
    private String beschrijving;
    @Column(name="prijs")
    private Double prijs;

    @Transient
    private String status;
    @Transient
    private Date lastUpdated;
    @ManyToMany
    @JoinTable(name="ov_chipkaart_product",
            joinColumns = @JoinColumn(name="product_nummer", referencedColumnName = "product_nummer"),
            inverseJoinColumns = @JoinColumn(name="kaart_nummer", referencedColumnName = "kaart_nummer")
    )
    List<OVChipkaart> ovChipkaarts;

    public Product() {
        this.ovChipkaarts = new ArrayList<OVChipkaart>();
    }

    public Product(Integer id, String naam, String beschrijving, Double prijs, String status, Date lastUpdated) {
        this.id = id;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.ovChipkaarts = new ArrayList<OVChipkaart>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }

    public void addChipkaart(OVChipkaart chipkaart) {
        ovChipkaarts.add(chipkaart);
    }

    public void removeChipkaart(OVChipkaart chipkaart) {
        ovChipkaarts.remove(chipkaart);
    }

    public List<OVChipkaart> getChipkaarts() {
        return this.ovChipkaarts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }

    public void setChipkaarts(List<OVChipkaart> products) {
        this.ovChipkaarts = products;
    }
}
