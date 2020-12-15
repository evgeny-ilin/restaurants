package club.beingsoft.restaurants.model;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "restaurants",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")},
        indexes = {@Index(columnList = "id, name", name = "id_name_idx")})
public class Restaurant extends AbstractNamedEntity {
    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
