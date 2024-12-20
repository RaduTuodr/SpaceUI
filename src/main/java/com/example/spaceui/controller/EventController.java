package com.example.spaceui.controller;

import com.example.spaceui.model.Event;
import com.example.spaceui.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
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
        return "events/create";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id).orElse(null);
        if (event != null) {
            model.addAttribute("event", event);
            return "events/edit";
        }
        return "redirect:/events";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute Event event) {
        eventService.updateEvent(id, event);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
}