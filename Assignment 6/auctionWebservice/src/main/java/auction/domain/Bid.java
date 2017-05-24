package auction.domain;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @XmlElement
    private FontysTime time;
    
    @Embedded
    @XmlElement(required = true)
    private Money amount;
    
    @ManyToOne
    @XmlElement(required = true)
    private User buyer;

    public Bid() {
        // Empty constructor used for JPA binding.
    }

    public Bid(User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
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
    
}
