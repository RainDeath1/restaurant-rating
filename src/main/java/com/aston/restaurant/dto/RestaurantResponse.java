
package com.aston.restaurant.dto;

public class RestaurantResponse {

    private Long id;
    private String name;
    private Long cityId;
    private String cityName;
    private double averageRating;

    public RestaurantResponse() {
    }

    public RestaurantResponse(
            Long id,
            String name,
            Long cityId,
            String cityName,
            double averageRating) {

        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.cityName = cityName;
        this.averageRating = averageRating;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public double getAverageRating() {
        return averageRating;
    }
}
