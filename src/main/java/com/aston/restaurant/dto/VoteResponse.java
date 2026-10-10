
package com.aston.restaurant.dto;

public class VoteResponse {

    private Long id;
    private Integer rating;
    private String comment;
    private Long restaurantId;

    public VoteResponse() {
    }

    public VoteResponse(
            Long id,
            Integer rating,
            String comment,
            Long restaurantId) {

        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.restaurantId = restaurantId;
    }

    public Long getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }
}
