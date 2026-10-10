
package com.aston.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RestaurantRequest {

    @NotBlank(message = "Restaurant name cannot be empty")
    @Size(max = 255)
    private String name;

    @NotNull(message = "City ID is required")
    @Positive(message = "City ID must be positive")
    private Long cityId;

    public RestaurantRequest() {
    }

    public RestaurantRequest(String name, Long cityId) {
        this.name = name;
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
