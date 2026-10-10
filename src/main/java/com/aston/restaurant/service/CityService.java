
package com.aston.restaurant.service;

import com.aston.restaurant.entity.City;
import com.aston.restaurant.repository.CityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "City not found with id: " + id
                ));
    }

    @Transactional
    public City createCity(City city) {
        city.setId(null);
        return cityRepository.save(city);
    }

    @Transactional
    public City updateCity(Long id, City updatedCity) {
        City city = getCityById(id);

        city.setName(updatedCity.getName());

        return cityRepository.save(city);
    }

    @Transactional
    public void deleteCity(Long id) {
        City city = getCityById(id);
        cityRepository.delete(city);
    }
}
