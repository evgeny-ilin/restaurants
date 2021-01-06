package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.QVote;
import club.beingsoft.restaurants.model.Restaurant;
import club.beingsoft.restaurants.model.Vote;
import club.beingsoft.restaurants.repository.jpa.RestaurantJpaRepository;
import club.beingsoft.restaurants.repository.jpa.VoteJpaRepository;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.VoteCantBeChangedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static club.beingsoft.restaurants.util.ValidationUtil.getFoundException;
import static club.beingsoft.restaurants.util.ValidationUtil.isAfterDeadLine;

@RestController
@RequestMapping(path = "/rest/votes", produces = "application/json")
@Transactional(readOnly = true)
@Validated
public class VoteController {

    @Autowired
    private VoteJpaRepository voteJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @GetMapping
    public List<Vote> getAll() {
        return (List<Vote>) voteJpaRepository.findAll(QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId()));
    }

    @GetMapping(path = "/restaurant/{id}")
    public List<Vote> getAllByRestaurant(@PathVariable @NotNull Integer id) {
        return (List<Vote>) voteJpaRepository.findAll(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.restaurant.id.eq(id))
        );
    }

    @GetMapping(path = "/{id}")
    public Vote get(@PathVariable @NotNull Integer id) {
        return voteJpaRepository.findOne(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.id.eq(id))
        ).orElseThrow(() -> getFoundException(Vote.class, id));
    }

    @PostMapping(path = "/")
    @Transactional
    public ResponseEntity<Vote> save(
            @RequestParam(name = "restaurant") @NotNull Integer restaurantId
    ) {
        Restaurant restaurant = restaurantJpaRepository.findById(restaurantId).orElseThrow(() -> getFoundException(Restaurant.class, restaurantId));
        if (restaurant.isDeleted()) {
            throw new VoteCantBeChangedException("Vote can't be changed due restaurant deleted");
        }

        LocalDate date = LocalDate.now();
        Optional<Vote> voteDB = voteJpaRepository.findOne(
                QVote.vote.user.id.eq(SecurityUtil.getAuthUser().getId())
                        .and(QVote.vote.voteDate.eq(date))
        );

        Vote vote;
        if (voteDB.isPresent()) {
            vote = voteDB.get();
            vote.undelete();
            if (isAfterDeadLine()) {
                throw new VoteCantBeChangedException("Vote can't be changed due deadline");
            }
        } else {
            vote = new Vote(restaurant, date);
        }

        boolean isNew = vote.isNew();
        voteJpaRepository.save(vote);
        if (isNew) {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/rest/votes/{id}")
                    .buildAndExpand(vote.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(vote);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
