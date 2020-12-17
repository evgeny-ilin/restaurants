package club.beingsoft.restaurants.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id", "vote_date"}, name = "votes_unique_user_restaurant_idx")})
public class Vote extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    public Vote() {
    }

    public Vote(@NotNull Restaurant restaurant, @NotNull LocalDate voteDate) {
        this(null, restaurant, voteDate);
    }

    public Vote(Integer id, @NotNull Restaurant restaurant, @NotNull LocalDate voteDate) {
        super(id);
        this.restaurant = restaurant;
        this.voteDate = voteDate;
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setUserForTest(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
