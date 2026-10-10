
package com.aston.restaurant.service;

import com.aston.restaurant.dto.RestaurantRequest;
import com.aston.restaurant.dto.RestaurantResponse;
import com.aston.restaurant.entity.City;
import com.aston.restaurant.entity.Restaurant;
import com.aston.restaurant.repository.CityRepository;
import com.aston.restaurant.repository.RestaurantJdbcRepository;
import com.aston.restaurant.repository.RestaurantRepository;

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
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RestaurantJdbcRepository restaurantJdbcRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void createRestaurant_shouldSaveRestaurant() {

        City city = new City("Moscow");
        city.setId(1L);

        RestaurantRequest request =
                new RestaurantRequest("KFC", 1L);

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        when(restaurantRepository.save(any(Restaurant.class)))
                .thenAnswer(invocation -> {
                    Restaurant restaurant = invocation.getArgument(0);
                    restaurant.setId(1L);
                    return restaurant;
                });

        RestaurantResponse response =
                restaurantService.createRestaurant(request);

        assertEquals(1L, response.getId());
        assertEquals("KFC", response.getName());
        assertEquals("Moscow", response.getCityName());
        assertEquals(0.0, response.getAverageRating());

        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void getRestaurantById_shouldReturnRestaurant() {

        City city = new City("Moscow");
        city.setId(1L);

        Restaurant restaurant = new Restaurant("KFC", city);
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        RestaurantResponse response =
                restaurantService.getRestaurantById(1L);

        assertEquals(1L, response.getId());
        assertEquals("KFC", response.getName());
        assertEquals("Moscow", response.getCityName());
    }

    @Test
    void getRestaurantById_shouldThrowNotFound() {

        when(restaurantRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restaurantService.getRestaurantById(999L)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );
    }

    @Test
    void updateRestaurant_shouldChangeName() {

        City city = new City("Moscow");
        city.setId(1L);

        Restaurant restaurant = new Restaurant("KFC", city);
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        when(restaurantRepository.save(restaurant))
                .thenReturn(restaurant);

        RestaurantRequest request =
                new RestaurantRequest("KFC Premium", 1L);

        RestaurantResponse response =
                restaurantService.updateRestaurant(1L, request);

        assertEquals("KFC Premium", response.getName());

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void deleteRestaurant_shouldDeleteRestaurant() {

        City city = new City("Moscow");
        city.setId(1L);

        Restaurant restaurant = new Restaurant("KFC", city);
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void getRestaurantsByCity_shouldUseJdbcRepository() {

        City city = new City("Moscow");
        city.setId(1L);

        RestaurantResponse restaurant =
                new RestaurantResponse(
                        1L, "KFC", 1L, "Moscow", 5.0
                );

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        when(restaurantJdbcRepository.findByCityOrderByRating(1L))
                .thenReturn(List.of(restaurant));

        List<RestaurantResponse> result =
                restaurantService.getRestaurantsByCity(1L);

        assertEquals(1, result.size());
        assertEquals("KFC", result.get(0).getName());

        verify(restaurantJdbcRepository)
                .findByCityOrderByRating(1L);
    }
}
