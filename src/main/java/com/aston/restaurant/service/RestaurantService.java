
package com.aston.restaurant.service;

import com.aston.restaurant.dto.RestaurantRequest;
import com.aston.restaurant.dto.RestaurantResponse;
import com.aston.restaurant.entity.City;
import com.aston.restaurant.entity.Restaurant;
import com.aston.restaurant.repository.CityRepository;
import com.aston.restaurant.repository.RestaurantRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.aston.restaurant.repository.RestaurantJdbcRepository;


import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;


    private final RestaurantJdbcRepository restaurantJdbcRepository;


    public RestaurantService(
            RestaurantRepository restaurantRepository,
            CityRepository cityRepository,
            RestaurantJdbcRepository restaurantJdbcRepository) {

        this.restaurantRepository = restaurantRepository;
        this.cityRepository = cityRepository;
        this.restaurantJdbcRepository = restaurantJdbcRepository;
    }


    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurantsByCity(Long cityId) {

        findCity(cityId);

        return restaurantJdbcRepository
                .findByCityOrderByRating(cityId);
    }


    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = findRestaurant(id);
        return toResponse(restaurant);
    }

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest request) {

        City city = findCity(request.getCityId());

        Restaurant restaurant = new Restaurant(
                request.getName(),
                city
        );

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return toResponse(savedRestaurant);
    }

    @Transactional
    public RestaurantResponse updateRestaurant(
            Long id,
            RestaurantRequest request) {

        Restaurant restaurant = findRestaurant(id);
        City city = findCity(request.getCityId());

        restaurant.setName(request.getName());
        restaurant.setCity(city);

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return toResponse(updatedRestaurant);
    }

    @Transactional
    public void deleteRestaurant(Long id) {

        Restaurant restaurant = findRestaurant(id);

        restaurantRepository.delete(restaurant);
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Restaurant not found with id: " + id
                ));
    }

    private City findCity(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "City not found with id: " + id
                ));
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {

        City city = restaurant.getCity();

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                city.getId(),
                city.getName(),
                restaurant.getAverageRating()
        );
    }
}
