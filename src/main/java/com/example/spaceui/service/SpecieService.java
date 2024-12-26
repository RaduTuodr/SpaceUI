package com.example.spaceui.service;

import com.example.spaceui.model.Species;
import com.example.spaceui.repository.SpeciesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecieService {

    private final SpeciesRepository speciesRepository;

    public SpecieService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    public List<Species> getAllSpecies() {
        return speciesRepository.findAll();
    }

    public Species addSpecies(Species species) {
        return speciesRepository.save(species);
    }

    public Optional<Species> getSpeciesById(Long id) {
        return speciesRepository.findById(id);
    }
}
