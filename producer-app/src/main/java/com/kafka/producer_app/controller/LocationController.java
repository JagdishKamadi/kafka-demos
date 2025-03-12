package com.kafka.producer_app.controller;

import com.kafka.producer_app.model.Location;
import com.kafka.producer_app.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = "/getLiveLocation")
    public ResponseEntity<Location> getLiveLocation() {

        return ResponseEntity.ok(locationService.getLocation());
    }
}
