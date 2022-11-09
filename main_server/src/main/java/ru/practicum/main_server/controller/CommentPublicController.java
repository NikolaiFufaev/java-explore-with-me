package ru.practicum.main_server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getCommentsByEvent(@Positive @PathVariable long eventId) {
        log.info("Get event comments Id={}", eventId);
        return commentService.getEventComments(eventId);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDto> getCommentsByUser(@Positive @PathVariable long userId) {
        log.info("Get user comments Id={}", userId);
        return commentService.getUserComments(userId);
    }

}
