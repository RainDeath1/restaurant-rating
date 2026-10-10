
package com.aston.restaurant.service;

import com.aston.restaurant.dto.VoteRequest;
import com.aston.restaurant.dto.VoteResponse;
import com.aston.restaurant.entity.Restaurant;
import com.aston.restaurant.entity.Vote;
import com.aston.restaurant.repository.RestaurantRepository;
import com.aston.restaurant.repository.VoteRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(
            VoteRepository voteRepository,
            RestaurantRepository restaurantRepository) {

        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<VoteResponse> getAllVotes() {
        return voteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VoteResponse getVoteById(Long id) {
        return toResponse(findVote(id));
    }

    @Transactional(readOnly = true)
    public List<VoteResponse> getVotesByRestaurantId(Long restaurantId) {
        findRestaurant(restaurantId);

        return voteRepository.findByRestaurant_Id(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public VoteResponse createVote(
            Long restaurantId,
            VoteRequest request) {

        Restaurant restaurant = findRestaurant(restaurantId);

        Vote vote = new Vote(
                request.getRating(),
                request.getComment(),
                restaurant
        );

        Vote savedVote = voteRepository.saveAndFlush(vote);

        recalculateRating(restaurantId);

        return toResponse(savedVote);
    }

    @Transactional
    public VoteResponse updateVote(Long id, VoteRequest request) {

        Vote vote = findVote(id);

        vote.setRating(request.getRating());
        vote.setComment(request.getComment());

        Vote updatedVote = voteRepository.saveAndFlush(vote);

        recalculateRating(vote.getRestaurant().getId());

        return toResponse(updatedVote);
    }

    @Transactional
    public void deleteVote(Long id) {

        Vote vote = findVote(id);
        Long restaurantId = vote.getRestaurant().getId();

        voteRepository.delete(vote);
        voteRepository.flush();

        recalculateRating(restaurantId);
    }

    private void recalculateRating(Long restaurantId) {

        Restaurant restaurant = findRestaurant(restaurantId);

        Double average = voteRepository
                .findAverageRatingByRestaurantId(restaurantId);

        restaurant.setAverageRating(
                average != null ? average : 0.0
        );

        restaurantRepository.save(restaurant);
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Restaurant not found with id: " + id
                ));
    }

    private Vote findVote(Long id) {
        return voteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vote not found with id: " + id
                ));
    }

    private VoteResponse toResponse(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getRating(),
                vote.getComment(),
                vote.getRestaurant().getId()
        );
    }
}
