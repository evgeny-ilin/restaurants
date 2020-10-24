package club.beingsoft.restaurants.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Dish.DELETE, query = "DELETE FROM Dish d WHERE d.id=:id AND d.user.id=:user_id"),
        @NamedQuery(name = Dish.GET_BETWEEN_HALF_OPEN, query = "SELECT d FROM Dish d WHERE d.user.id=:user_id AND d.editDate >= :start_time AND d.editDate < :end_time ORDER BY d.editDate DESC"),
        @NamedQuery(name = Dish.GET_ALL, query = "SELECT d FROM Dish d WHERE d.user.id=:user_id ORDER BY d.editDate DESC"),
        @NamedQuery(name = Dish.GET, query = "SELECT d FROM Dish d WHERE d.id=:id AND d.user.id=:user_id"),
})
@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "unique_dish_name_idx")})
public class Dish extends AbstractNamedEntity {
    public static final String DELETE = "Dish.delete";
    public static final String GET_ALL = "Dish.getAll";
    public static final String GET = "Dish.get";
    public static final String GET_BETWEEN_HALF_OPEN = "Dish.getBetweenHalfOpen";

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
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
