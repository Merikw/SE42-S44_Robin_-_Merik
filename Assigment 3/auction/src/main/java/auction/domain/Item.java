package auction.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
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
import nl.fontys.util.Money;

@Entity
@NamedQueries({
    @NamedQuery(name = "Item.count", query = "select count(i) from Item as i")
    ,
    @NamedQuery(name = "Item.findById", query = "select i from Item as i where i.id = :id")
    ,
    @NamedQuery(name = "Item.getAll", query = "select i from Item as i")
    ,
    @NamedQuery(name = "Item.findByDescription", query = "select i from Item as i where i.description = :description"),})

public class Item implements Comparable<Item>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne (cascade = {CascadeType.ALL})
    private User seller;
    @OneToOne (cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Bid highest;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "description", column = @Column(name = "cat_description"))
    })
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
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
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
