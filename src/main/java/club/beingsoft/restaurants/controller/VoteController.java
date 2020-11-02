package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.QVote;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.repository.jpa.VoteJpaRepository;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.VoteCantBeChanged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/votes")
public class VoteController {

    @Autowired
    private VoteJpaRepository voteJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping(produces = "application/json")
    public List<Vote> getAllVotes() {
        return (List<Vote>) voteJpaRepository.findAll(QVote.vote.user.eq(SecurityUtil.getAuthUser()));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Vote getVote(@PathVariable Integer id) {
        return voteJpaRepository.findOne(
                QVote.vote.user.eq(SecurityUtil.getAuthUser())
                        .and(QVote.vote.id.eq(id))
        ).get();
    }

    @PostMapping(path = "/", produces = "application/json")
    public ResponseEntity<Object> saveVote(
            @RequestParam(name = "restaurant") Integer restaurantId
    ) {
        Restaurant restaurant = restaurantJpaRepository.findById(restaurantId).get();
        LocalDate date = LocalDate.now();

        Optional<Vote> voteDB = voteJpaRepository.findOne(
                QVote.vote.user.eq(SecurityUtil.getAuthUser())
                        .and(QVote.vote.voteDate.eq(date)));
        Vote vote;
        if (voteDB.isPresent()) {
            vote = voteDB.get();
            LocalDateTime deadLine = vote.getVoteDate().atTime(11, 00);
            LocalDateTime midnightDayBefore = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
            if (!(deadLine.isAfter(midnightDayBefore)
                    && deadLine.isAfter(LocalDateTime.now()))
            )
                throw new VoteCantBeChanged("Vote can't be changed");
            vote.setRestaurant(restaurant);
        } else
            vote = new Vote(restaurant, date);

        return new ResponseEntity(voteJpaRepository.save(vote), HttpStatus.CREATED);
    }
}
