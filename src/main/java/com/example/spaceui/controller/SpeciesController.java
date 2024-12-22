package com.example.spaceui.controller;

import com.example.spaceui.model.Species;
import com.example.spaceui.service.SpecieService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/species")
public class SpeciesController {

    private final SpecieService specieService;

    public SpeciesController(SpecieService specieService) {
        this.specieService = specieService;
    }

    @PostMapping
    public Species addSpecies(@RequestBody Species species) {
        return specieService.addSpecies(species);
    }
}
