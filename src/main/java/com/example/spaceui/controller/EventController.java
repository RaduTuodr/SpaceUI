package com.example.spaceui.controller;

import com.example.spaceui.model.Event;
import com.example.spaceui.service.EventService;
import com.example.spaceui.service.PlanetService;
import com.example.spaceui.service.SpecieService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        model.addAttribute("events", eventsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventsPage.getTotalPages());
        model.addAttribute("keyword", keyword);

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
