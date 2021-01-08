package club.beingsoft.restaurants.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurants",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Menu> menu;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}
