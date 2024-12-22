package com.example.spaceui.controller;

import com.example.spaceui.model.Planet;
import com.example.spaceui.service.PlanetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private final PlanetService planetService;

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public Planet addPlanet(@RequestBody Planet planet) {
        return planetService.addPlanet(planet);
    }
}
