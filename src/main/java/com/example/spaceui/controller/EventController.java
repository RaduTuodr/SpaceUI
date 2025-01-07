package com.example.spaceui.controller;

import com.example.spaceui.model.Event;
import com.example.spaceui.model.Planet;
import com.example.spaceui.model.Species;
import com.example.spaceui.service.EventService;
import com.example.spaceui.service.PlanetService;
import com.example.spaceui.service.SpecieService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final PlanetService planetService;
    private final SpecieService specieService;

    public EventController(EventService eventService, PlanetService planetService, SpecieService specieService) {
        this.eventService = eventService;
        this.planetService = planetService;
        this.specieService = specieService;
    }

    @GetMapping
    public String getAllEvents(
            Model model,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Event> eventsPage = eventService.findAllEvents(keyword, page);

        Map<Long, String> planetNamesMap = new HashMap<>();
        Map<Long, String> speciesNamesMap = new HashMap<>();

        for (Event event : eventsPage.getContent()) {
            try {
                if (event.getPlanetId() == null) {
                    planetNamesMap.put(event.getId(), "-");
                    continue;
                }
                Optional<Planet> planet = planetService.getPlanetById(event.getPlanetId());
                planetNamesMap.put(event.getId(), planet.map(Planet::getName).orElse("-"));
            } catch (IllegalArgumentException e) {
                System.out.println("Planet not found");
            }

            try {
                System.out.println(event.getSpeciesId());
                if (event.getSpeciesId() == null) {
                    speciesNamesMap.put(event.getId(), "-");
                    continue;
                }
                Optional<Species> species = specieService.getSpeciesById(event.getSpeciesId());
                speciesNamesMap.put(event.getId(), species.map(Species::getName).orElse("-"));
            } catch (IllegalArgumentException e) {
                System.out.println("Species not found");
            }
        }

        model.addAttribute("events", eventsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventsPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("planets", planetNamesMap);
        model.addAttribute("species", speciesNamesMap);

        return "events/list";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("planets", planetService.getAllPlanets());
        model.addAttribute("species", specieService.getAllSpecies());
        return "events/create";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Event event,
                              @RequestParam Long planetId,
                              @RequestParam Long speciesId) {
        event.setPlanetId(planetId);
        event.setSpeciesId(speciesId);
        eventService.createEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found for ID: " + id));
        model.addAttribute("event", event);
        model.addAttribute("planets", planetService.getAllPlanets());
        model.addAttribute("species", specieService.getAllSpecies());
        return "events/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable Long id,
                              @ModelAttribute Event event,
                              @RequestParam Long planetId,
                              @RequestParam Long speciesId) {
        event.setPlanetId(planetId);
        event.setSpeciesId(speciesId);
        eventService.updateEvent(id, event);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
}
