package auction.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.fontys.util.Money;

@Entity
@NamedQueries({
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i"),
    @NamedQuery(name = "Item.findById", query = "select i from Item as i where i.id = :id"),
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i"),
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description")})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Item implements Comparable<Item>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @XmlAttribute(required = true)
    private String description;
    
    @ManyToOne
    @XmlElement(required = true)
    private User seller;
    
    @OneToOne
    @XmlElement(name = "highest")
    private Bid highest;
    
    @Embedded    
    @AttributeOverride(name = "description", column = @Column(name = "cat_description"))
    @XmlElement(required = true)
    private Category category;

    public Item() {
        // Empty constructor used for JPA binding.
    }

    public Item(User seller, Category category, String description) {
        this.seller = seller;
        this.category = category;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Bid getHighestBid() {
        return highest;
    }

    public Bid newBid(User buyer, Money amount) {
        if (this.getHighestBid() != null && this.getHighestBid().getAmount().compareTo(amount) >= 0) {
            return null;
        }
        highest = new Bid(buyer, amount);
        return highest;
    }

    @Override
    public int compareTo(Item o) {
        if (this.id > o.id) {
            return 1;
        } else if (this.id.equals(o.id)) {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Item other = (Item) obj;

        return Objects.equals(this.id, other.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
