package club.beingsoft.restaurants.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {

}
