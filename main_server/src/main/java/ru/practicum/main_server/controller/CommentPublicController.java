package ru.practicum.main_server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getCommentsByEvent(@Positive @PathVariable long eventId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        log.info("Get event comments Id={}", eventId);
        return commentService.getEventComments(eventId, from, size);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDto> getCommentsByUser(@Positive @PathVariable long userId,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        log.info("Get user comments Id={}", userId);
        return commentService.getUserComments(userId, from, size);
    }

}
