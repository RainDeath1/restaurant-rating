
package com.aston.restaurant.repository;

import com.aston.restaurant.dto.RestaurantResponse;
import com.aston.restaurant.entity.City;
import com.aston.restaurant.entity.Restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RestaurantJdbcRepositoryTest {

    @Autowired
    private RestaurantJdbcRepository restaurantJdbcRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void findByCityOrderByRating_shouldSortRestaurants() {

        City moscow = cityRepository.saveAndFlush(
                new City("Moscow")
        );

        City spb = cityRepository.saveAndFlush(
                new City("Saint Petersburg")
        );

        Restaurant kfc = saveRestaurant(
                "KFC", moscow, 3.0
        );

        Restaurant burgerKing = saveRestaurant(
                "Burger King", moscow, 5.0
        );

        Restaurant pizza = saveRestaurant(
                "Pizza", moscow, 4.0
        );

        saveRestaurant("Other Restaurant", spb, 5.0);

        List<RestaurantResponse> result =
                restaurantJdbcRepository
                        .findByCityOrderByRating(moscow.getId());

        assertEquals(3, result.size());

        assertEquals(
                List.of(
                        burgerKing.getId(),
                        pizza.getId(),
                        kfc.getId()
                ),
                result.stream()
                        .map(RestaurantResponse::getId)
                        .toList()
        );

        assertEquals(5.0, result.get(0).getAverageRating());
        assertEquals(4.0, result.get(1).getAverageRating());
        assertEquals(3.0, result.get(2).getAverageRating());

        assertTrue(result.stream().allMatch(
                restaurant ->
                        restaurant.getCityId().equals(moscow.getId())
        ));
    }

    @Test
    void findByCityOrderByRating_shouldReturnEmptyList() {

        City city = cityRepository.saveAndFlush(
                new City("Kazan")
        );

        List<RestaurantResponse> result =
                restaurantJdbcRepository
                        .findByCityOrderByRating(city.getId());

        assertTrue(result.isEmpty());
    }

    private Restaurant saveRestaurant(
            String name,
            City city,
            double rating) {

        Restaurant restaurant = new Restaurant(name, city);
        restaurant.setAverageRating(rating);

        return restaurantRepository.saveAndFlush(restaurant);
    }
}
