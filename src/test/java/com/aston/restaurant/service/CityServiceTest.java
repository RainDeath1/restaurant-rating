
package com.aston.restaurant.service;

import com.aston.restaurant.entity.City;
import com.aston.restaurant.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void createCity_shouldSaveCity() {
        City city = new City("Moscow");

        City savedCity = new City("Moscow");
        savedCity.setId(1L);

        when(cityRepository.save(city)).thenReturn(savedCity);

        City result = cityService.createCity(city);

        assertEquals(1L, result.getId());
        assertEquals("Moscow", result.getName());

        verify(cityRepository).save(city);
    }

    @Test
    void getCityById_shouldReturnCity() {
        City city = new City("Moscow");
        city.setId(1L);

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        City result = cityService.getCityById(1L);

        assertEquals("Moscow", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void getCityById_shouldThrowNotFound() {
        when(cityRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> cityService.getCityById(999L)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );
    }

    @Test
    void updateCity_shouldChangeName() {
        City city = new City("Moscow");
        city.setId(1L);

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        when(cityRepository.save(city)).thenReturn(city);

        City updated = cityService.updateCity(
                1L,
                new City("Saint Petersburg")
        );

        assertEquals("Saint Petersburg", updated.getName());
        verify(cityRepository).save(city);
    }

    @Test
    void deleteCity_shouldDeleteExistingCity() {
        City city = new City("Moscow");
        city.setId(1L);

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        cityService.deleteCity(1L);

        verify(cityRepository).delete(city);
    }
}
