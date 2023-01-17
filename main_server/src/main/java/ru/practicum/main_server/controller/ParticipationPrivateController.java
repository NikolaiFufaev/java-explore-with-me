package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.service.ParticipationService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
public class ParticipationPrivateController {

    private final ParticipationService participationService;

    public ParticipationPrivateController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequestsByUser(@PathVariable Long userId) {
        log.info("get participation requests by  userId{}", userId);
        return participationService.getParticipationRequestsByUser(userId);
    }

    @PostMapping
    public ParticipationRequestDto addParticipationRequest(
            @PathVariable Long userId,
            @Validated @RequestParam Long eventId)  {
        log.info("add participation requests by userId{} eventId {}", userId, eventId);
        return participationService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("cancel participation requests by userId{} requestId {}", userId, requestId);
        return participationService.cancelRequestByUser(userId, requestId);
    }
}
