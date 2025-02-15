package com.example.spaceui.service;

import com.example.spaceui.model.Planet;
import com.example.spaceui.repository.PlanetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    public Planet addPlanet(Planet planet) {
        return planetRepository.save(planet);
    }

    public Optional<Planet> getPlanetById(Long planetId) {
        return planetRepository.findById(planetId);
    }
}
