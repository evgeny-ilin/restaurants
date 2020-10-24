//package club.beingsoft.restaurants.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import java.util.Date;
//
//@Entity
//@Table(name = "votes_history")
//public class VoteHistory extends Vote {
//    @Column(name = "start_date", nullable = false)
//    @NotNull
//    private Date startDate;
//
//    @Column(name = "end_date", nullable = false)
//    @NotNull
//    private Date endDate;
//
//    public VoteHistory() {
//    }
//
//    public VoteHistory(@NotNull Integer restaurantId, @NotNull Date voteDate, @NotNull Date startDate, @NotNull Date endDate) {
//        this(null, restaurantId, voteDate, startDate, endDate);
//    }
//
//    public VoteHistory(Integer id, @NotNull Integer restaurantId, @NotNull Date voteDate, @NotNull Date startDate, @NotNull Date endDate) {
//        super(id, restaurantId, voteDate);
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
