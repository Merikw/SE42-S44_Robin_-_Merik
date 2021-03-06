package auction.domain;

import javax.persistence.Entity;

@Entity
public class Painting extends Item {

    private String title;
    private String painter;

    public Painting() {
        // Empty constructor used for JPA binding
    }

    public Painting(String title, String painter, User seller, Category category, String description) {
        super(seller, category, description);
        this.title = title;
        this.painter = painter;
    }

    public String getTitle() {
        return title;
    }

    public String getPainter() {
        return painter;
    }

}
