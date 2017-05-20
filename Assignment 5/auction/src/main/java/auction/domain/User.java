package auction.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "account") // 'USER' is a reserver SQL-keyword so we chose an alternative name
@NamedQueries({
    @NamedQuery(name = "User.count", query = "select count(u) from User as u"),
    @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :email"),
    @NamedQuery(name = "User.getAll", query = "select u from User as u"),})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.PERSIST)
    private Set<Item> offeredItems;

    public User() {
        // Empty constructor used for JPA binding.
    }

    public User(String email) {
        this.email = email;

        offeredItems = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Iterator<Item> getOfferedItems() {
        return offeredItems.iterator();
    }

    public int numberOfOfferedItems() {
        return offeredItems.size();
    }

    void addItem(Item item) {
        offeredItems.add(item);
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

        final User other = (User) obj;

        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

}
