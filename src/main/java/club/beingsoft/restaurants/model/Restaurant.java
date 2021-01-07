package club.beingsoft.restaurants.model;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@SqlResultSetMapping(
        name = "getMostScoredMapping", // If @Query does not specify name, the method name will be used by default.
        classes = {
                @ConstructorResult(
                        targetClass = club.beingsoft.restaurants.to.RestaurantWithVotesTo.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                @ColumnResult(name = "NAME", type = String.class),
                                @ColumnResult(name = "VCOUNT", type = BigInteger.class)
                        }
                )
        }
)
@NamedNativeQuery(name = "Restaurant.getMostScored",
        query = "SELECT RESTAURANT_ID AS ID, NAME AS NAME, COUNT(V.ID) AS VCOUNT\n" +
                "FROM RESTAURANTS\n" +
                "         JOIN VOTES V on RESTAURANTS.ID = V.RESTAURANT_ID\n" +
                "WHERE V.DELETE_DATE IS NULL\n" +
                "  AND RESTAURANTS.DELETE_DATE IS NULL\n" +
                "  AND VOTE_DATE = ?1\n" +
                "GROUP BY RESTAURANT_ID, NAME\n" +
                "ORDER BY VCOUNT DESC",
        resultSetMapping = "getMostScoredMapping")
@Table(name = "restaurants",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {
    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
