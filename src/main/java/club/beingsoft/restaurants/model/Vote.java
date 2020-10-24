package club.beingsoft.restaurants.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"}, name = "votes_unique_user_restaurant_idx")})
public class Vote extends AbstractBaseEntity {
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private Date voteDate;

    public Vote() {
    }

    public Vote(@NotNull Integer restaurantId, @NotNull Date voteDate) {
        this(null, restaurantId, voteDate);
    }

    public Vote(Integer id, @NotNull Integer restaurantId, @NotNull Date voteDate) {
        super(id);
        restaurant.setId(restaurantId);
        this.voteDate = voteDate;
    }

    public Integer getRestaurantId() {
        return this.id;
    }

    public void setRestaurantId(Integer restaurantId) {
        restaurant.setId(restaurantId);
    }

    public Date getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(Date voteDate) {
        this.voteDate = voteDate;
    }
}
