package club.beingsoft.restaurants.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dishes")
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
        return super.toString() +
                "price=" + price +
                ", menus=" + menus;
    }
}
