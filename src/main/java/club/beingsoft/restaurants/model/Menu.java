package club.beingsoft.restaurants.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menus", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menus_unique_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "MENU_DISHES",
            joinColumns = {@JoinColumn(name = "dish_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")}
    )
    private Set<Dish> dishes = new HashSet<>();

    public Menu() {
    }

    public Menu(@NotNull LocalDate menuDate, @NotNull Integer restaurantId) {
        this(null, menuDate, restaurantId);
    }

    public Menu(Integer id, @NotNull LocalDate menuDate, @NotNull Integer restaurantId) {
        super(id);
        this.menuDate = menuDate;
        restaurant.setId(restaurantId);
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    public void removeDish(Set<Dish> dishes) {
        this.dishes.removeAll(dishes);
    }
}
