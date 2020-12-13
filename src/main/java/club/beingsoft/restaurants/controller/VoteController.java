package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.QVote;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.repository.jpa.VoteJpaRepository;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import club.beingsoft.restaurants.util.exception.VoteCantBeChangedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/votes")
@Transactional(readOnly = true)
@Validated
public class VoteController {

    @Autowired
    private VoteJpaRepository voteJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private Clock clock;

    @GetMapping(produces = "application/json")
    public List<Vote> getAllVotes() {
        return (List<Vote>) voteJpaRepository.findAll(QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId()));
    }

    @GetMapping(path = "/restaurant/{id}", produces = "application/json")
    public List<Vote> getAllVotesByRestaurant(@PathVariable @NotNull Integer id) {
        return (List<Vote>) voteJpaRepository.findAll(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.restaurant.id.eq(id))
        );
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Vote getVote(@PathVariable @NotNull Integer id) {
        return voteJpaRepository.findOne(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.id.eq(id))
        ).orElseThrow(() -> new NotFoundException(Vote.class, id));
    }

    @PostMapping(path = "/", produces = "application/json")
    @Transactional
    public ResponseEntity saveVote(
            @RequestParam(name = "restaurant") @NotNull Integer restaurantId
    ) {
        LocalDate date = LocalDate.now(clock);
        Optional<Vote> voteDB = voteJpaRepository.findOne(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.voteDate.eq(date))
                        .and(QVote.vote.restaurant.id.eq(restaurantId))
        );

        Vote vote;
        if (voteDB.isPresent()) {
            vote = voteDB.get();
            LocalDateTime deadLine = vote.getVoteDate().atTime(11, 0);
            if (deadLine.isBefore(LocalDateTime.now(clock)))
                throw new VoteCantBeChangedException("Vote can't be changed due deadline");
        } else {
            vote = new Vote(entityManager.getReference(Restaurant.class, restaurantId), date);
        }

        return new ResponseEntity(voteJpaRepository.save(vote), HttpStatus.CREATED);
    }
}
