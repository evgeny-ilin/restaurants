package club.beingsoft.restaurants.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menus",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menus_unique_restaurant_date_idx")},
        indexes = {@Index(columnList = "menu_date", name = "menu_date_idx")})
public class Menu extends AbstractBaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @JsonManagedReference
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MENU_DISHES",
            joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns = {@JoinColumn(name = "dish_id")}
    )
    @JsonManagedReference
    private Set<Dish> dishes = new HashSet<>();

    public Menu() {
    }

    public Menu(@NotNull LocalDate menuDate, @NotNull Restaurant restaurant) {
        this(null, menuDate, restaurant);
    }

    public Menu(Integer id, @NotNull LocalDate menuDate, @NotNull Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    public void removeDish(Set<Dish> dishes) {
        this.dishes.removeAll(dishes);
    }
}
