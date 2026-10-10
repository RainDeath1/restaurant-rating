
package com.aston.restaurant.controller;

import com.aston.restaurant.dto.RestaurantRequest;
import com.aston.restaurant.dto.RestaurantResponse;
import com.aston.restaurant.service.RestaurantService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        return ResponseEntity.ok(
                restaurantService.getAllRestaurants()
        );
    }


    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(
            @PathVariable("cityId") Long cityId) {

        return ResponseEntity.ok(
                restaurantService.getRestaurantsByCity(cityId)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(
                restaurantService.getRestaurantById(id)
        );
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest request) {

        RestaurantResponse response =
                restaurantService.createRestaurant(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable("id") Long id,
            @Valid @RequestBody RestaurantRequest request) {

        return ResponseEntity.ok(
                restaurantService.updateRestaurant(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable("id") Long id) {

        restaurantService.deleteRestaurant(id);

        return ResponseEntity.noContent().build();
    }
}
