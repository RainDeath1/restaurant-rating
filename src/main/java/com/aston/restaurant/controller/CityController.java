
package com.aston.restaurant.controller;

import com.aston.restaurant.entity.City;
import com.aston.restaurant.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) {
        City createdCity = cityService.createCity(city);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdCity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(
            @PathVariable("id") Long id,
            @Valid @RequestBody City city) {

        return ResponseEntity.ok(
                cityService.updateCity(id, city)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(
            @PathVariable("id") Long id) {

        cityService.deleteCity(id);

        return ResponseEntity.noContent().build();
    }
}
