package club.beingsoft.restaurants.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "unique_dish_name_idx")})
public class Dish extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 0, max = 100000)
    private BigDecimal price;

    @ManyToMany(mappedBy = "dishes")
    private Set<Menu> menus = new HashSet<>();

    public Dish() {
    }

    public Dish(Menu menu, String name, BigDecimal price) {
        this(null, menu, name, price);
    }

    public Dish(Integer id, Menu menu, String name, BigDecimal price) {
        super(id, name);
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Dish {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", editUser=" + user +
                ", editDate=" + editDate +
                '}';
    }
}
