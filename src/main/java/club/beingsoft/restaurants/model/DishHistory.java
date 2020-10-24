//package club.beingsoft.restaurants.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//import java.util.Date;
//
//@Entity
//@Table(name = "dishes_history")
//public class DishHistory extends Dish {
//    @Column(name = "start_date", nullable = false)
//    @NotNull
//    private Date startDate;
//
//    @Column(name = "end_date", nullable = false)
//    @NotNull
//    private Date endDate;
//
//    public DishHistory() {
//    }
//
//    public DishHistory(Integer menuId, String name, BigDecimal price, @NotNull Date startDate, @NotNull Date endDate) {
//        this(null, menuId, name, price, startDate, endDate);
//    }
//
//    public DishHistory(Integer id, Integer menuId, String name, BigDecimal price, @NotNull Date startDate, @NotNull Date endDate) {
//        super(id, menuId, name, price);
//        this.startDate = startDate;
//        this.endDate = endDate;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//}
