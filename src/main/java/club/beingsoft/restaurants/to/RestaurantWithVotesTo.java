package club.beingsoft.restaurants.to;

public class RestaurantWithVotesTo extends BaseTo {
    private Long votes;

    public RestaurantWithVotesTo(Integer id, String name, Long votes) {
        super(id, name);
        this.votes = votes;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantWithVotesTo)) return false;
        if (!super.equals(o)) return false;

        RestaurantWithVotesTo that = (RestaurantWithVotesTo) o;

        return votes != null ? votes.equals(that.votes) : that.votes == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        return result;
    }
}
