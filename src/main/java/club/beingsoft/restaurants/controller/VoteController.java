package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.QVote;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.repository.jpa.VoteJpaRepository;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.VoteCantBeChangedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static club.beingsoft.restaurants.util.ValidationUtil.checkEntityNotNull;
import static club.beingsoft.restaurants.util.ValidationUtil.checkId;

@RestController
@RequestMapping(path = "/rest/votes")
public class VoteController {

    @Autowired
    private VoteJpaRepository voteJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    private static final String VOTE_ENTITY = "Vote";
    @Autowired
    private Clock clock;

    @GetMapping(produces = "application/json")
    public List<Vote> getAllVotes() {
        return (List<Vote>) voteJpaRepository.findAll(QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId()));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Vote getVote(@PathVariable Integer id) {
        checkId(VOTE_ENTITY, id);
        Optional<Vote> vote = voteJpaRepository.findOne(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.id.eq(id))
        );
        checkEntityNotNull(VOTE_ENTITY, vote, id);
        return vote.get();
    }

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity<Object> saveVote(
            @RequestParam(name = "restaurant") Integer restaurantId
    ) {
        checkId("restaurant", restaurantId);
        Optional<Restaurant> restaurant = restaurantJpaRepository.findById(restaurantId);
        checkEntityNotNull(VOTE_ENTITY, restaurant, restaurantId);

        LocalDate date = LocalDate.now(clock);
        Optional<Vote> voteDB = voteJpaRepository.findOne(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.voteDate.eq(date))
                        .and(QVote.vote.restaurant.eq(restaurant.get()))
        );

        Vote vote;
        if (voteDB.isPresent()) {
            vote = voteDB.get();
            LocalDateTime deadLine = vote.getVoteDate().atTime(11, 00);
            if (deadLine.isBefore(LocalDateTime.now(clock)))
                throw new VoteCantBeChangedException("Vote can't be changed");
        } else
            vote = new Vote(restaurant.get(), date);

        return new ResponseEntity(voteJpaRepository.save(vote), HttpStatus.CREATED);
    }

}
