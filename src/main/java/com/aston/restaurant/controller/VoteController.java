
package com.aston.restaurant.controller;

import com.aston.restaurant.dto.VoteRequest;
import com.aston.restaurant.dto.VoteResponse;
import com.aston.restaurant.service.VoteService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/api/votes")
    public ResponseEntity<List<VoteResponse>> getAllVotes() {
        return ResponseEntity.ok(
                voteService.getAllVotes()
        );
    }

    @GetMapping("/api/votes/{id}")
    public ResponseEntity<VoteResponse> getVoteById(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                voteService.getVoteById(id)
        );
    }

    @GetMapping("/api/restaurants/{restaurantId}/votes")
    public ResponseEntity<List<VoteResponse>> getVotesByRestaurant(
            @PathVariable("restaurantId") Long restaurantId) {

        return ResponseEntity.ok(
                voteService.getVotesByRestaurantId(restaurantId)
        );
    }

    @PostMapping("/api/restaurants/{restaurantId}/votes")
    public ResponseEntity<VoteResponse> createVote(
            @PathVariable("restaurantId") Long restaurantId,
            @Valid @RequestBody VoteRequest request) {

        VoteResponse response =
                voteService.createVote(restaurantId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/api/votes/{id}")
    public ResponseEntity<VoteResponse> updateVote(
            @PathVariable("id") Long id,
            @Valid @RequestBody VoteRequest request) {

        return ResponseEntity.ok(
                voteService.updateVote(id, request)
        );
    }

    @DeleteMapping("/api/votes/{id}")
    public ResponseEntity<Void> deleteVote(
            @PathVariable("id") Long id) {

        voteService.deleteVote(id);

        return ResponseEntity.noContent().build();
    }
}
