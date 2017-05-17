package auction.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@Entity
@NamedQueries({
    @NamedQuery(name = "Bid.count", query = "select count(b) from Bid as b"),
    @NamedQuery(name = "Bid.findById", query = "select b from Bid as b where b.id = :id"),
    @NamedQuery(name = "Bid.getAll", query = "select b from Bid as b")})
public class Bid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @Column(name = "bid_time") // 'TIME' is a reserver SQL-keyword so we chose an alternative name
    private FontysTime time;
    
    @Embedded
    private Money amount;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User buyer;
    
    @OneToOne(mappedBy = "highest", cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Item item;

    public Bid() {
        // Empty constructor used for JPA binding.
    }

    public Bid(User buyer, Money amount, Item item) {
        this.buyer = buyer;
        this.amount = amount;
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }

    public Item getItem() {
        return item;
    }

}
