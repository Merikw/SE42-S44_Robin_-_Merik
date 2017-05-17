package auction.domain;

import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-17T12:58:11")
@StaticMetamodel(Item.class)
public class Item_ { 

    public static volatile SingularAttribute<Item, User> seller;
    public static volatile SingularAttribute<Item, Bid> highest;
    public static volatile SingularAttribute<Item, String> description;
    public static volatile SingularAttribute<Item, Long> id;
    public static volatile SingularAttribute<Item, Category> category;

}