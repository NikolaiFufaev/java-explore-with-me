package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.AdminUpdateEventRequest;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.model.State;
import ru.practicum.main_server.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
public class EventAdminController {

    private final EventService eventService;

    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<State> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size)
    {
        log.info("get admin events");

        return eventService.getAdminEvents(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(
            @PathVariable Long eventId,
            @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("update eventId {} by admin", eventId);
        return eventService.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEventByAdmin(@PathVariable Long eventId) {
        log.info("publish eventId {} by admin", eventId);
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEventByAdmin(@PathVariable Long eventId) {
        log.info("reject eventId {} by admin", eventId);
        return eventService.rejectEventByAdmin(eventId);
    }

}
