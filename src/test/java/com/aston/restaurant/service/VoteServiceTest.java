
package com.aston.restaurant.service;

import com.aston.restaurant.dto.VoteRequest;
import com.aston.restaurant.dto.VoteResponse;
import com.aston.restaurant.entity.City;
import com.aston.restaurant.entity.Restaurant;
import com.aston.restaurant.entity.Vote;
import com.aston.restaurant.repository.RestaurantRepository;
import com.aston.restaurant.repository.VoteRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private VoteService voteService;

    private Restaurant createRestaurant() {
        City city = new City("Moscow");
        city.setId(1L);

        Restaurant restaurant = new Restaurant("KFC", city);
        restaurant.setId(1L);

        return restaurant;
    }

    @Test
    void createVote_shouldUpdateAverageRating() {

        Restaurant restaurant = createRestaurant();
        VoteRequest request = new VoteRequest(5, "Excellent!");

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        when(voteRepository.saveAndFlush(any(Vote.class)))
                .thenAnswer(invocation -> {
                    Vote vote = invocation.getArgument(0);
                    vote.setId(1L);
                    return vote;
                });

        when(voteRepository.findAverageRatingByRestaurantId(1L))
                .thenReturn(5.0);

        VoteResponse response = voteService.createVote(1L, request);

        assertEquals(5, response.getRating());
        assertEquals(5.0, restaurant.getAverageRating(), 0.001);

        verify(voteRepository).saveAndFlush(any(Vote.class));
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void updateVote_shouldRecalculateRating() {

        Restaurant restaurant = createRestaurant();

        Vote vote = new Vote(3, "Average", restaurant);
        vote.setId(1L);

        when(voteRepository.findById(1L))
                .thenReturn(Optional.of(vote));

        when(voteRepository.saveAndFlush(vote))
                .thenReturn(vote);

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        when(voteRepository.findAverageRatingByRestaurantId(1L))
                .thenReturn(4.5);

        VoteRequest request = new VoteRequest(5, "Excellent!");

        VoteResponse response = voteService.updateVote(1L, request);

        assertEquals(5, response.getRating());
        assertEquals("Excellent!", response.getComment());
        assertEquals(4.5, restaurant.getAverageRating(), 0.001);

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void deleteVote_shouldRecalculateRating() {

        Restaurant restaurant = createRestaurant();

        Vote vote = new Vote(4, "Good", restaurant);
        vote.setId(2L);

        when(voteRepository.findById(2L))
                .thenReturn(Optional.of(vote));

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        when(voteRepository.findAverageRatingByRestaurantId(1L))
                .thenReturn(5.0);

        voteService.deleteVote(2L);

        assertEquals(5.0, restaurant.getAverageRating(), 0.001);

        verify(voteRepository).delete(vote);
        verify(voteRepository).flush();
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void deleteLastVote_shouldResetRatingToZero() {

        Restaurant restaurant = createRestaurant();
        restaurant.setAverageRating(5.0);

        Vote vote = new Vote(5, "Excellent", restaurant);
        vote.setId(1L);

        when(voteRepository.findById(1L))
                .thenReturn(Optional.of(vote));

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        voteService.deleteVote(1L);

        assertEquals(0.0, restaurant.getAverageRating(), 0.001);

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void getVotesByRestaurantId_shouldReturnVotes() {

        Restaurant restaurant = createRestaurant();

        Vote vote = new Vote(5, "Excellent", restaurant);
        vote.setId(1L);

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        when(voteRepository.findByRestaurant_Id(1L))
                .thenReturn(List.of(vote));

        List<VoteResponse> result =
                voteService.getVotesByRestaurantId(1L);

        assertEquals(1, result.size());
        assertEquals("Excellent", result.get(0).getComment());
    }

    @Test
    void createVote_shouldThrowWhenRestaurantNotFound() {

        when(restaurantRepository.findById(999L))
                .thenReturn(Optional.empty());

        VoteRequest request = new VoteRequest(5, "Excellent");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> voteService.createVote(999L, request)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );

        verify(voteRepository, never())
                .saveAndFlush(any(Vote.class));
    }
}
