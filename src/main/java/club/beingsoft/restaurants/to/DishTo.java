package club.beingsoft.restaurants.to;

import club.beingsoft.restaurants.model.Menu;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class DishTo extends BaseNamedTo {
    @Range(min = 0, max = 100000)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    private BigDecimal price;

    private Set<Menu> menus = new HashSet<>();

    public DishTo() {
    }

    public DishTo(Integer id, String name, BigDecimal price, Set<Menu> menus) {
        super(id, name);
        this.price = price;
        this.menus = menus;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }
}
