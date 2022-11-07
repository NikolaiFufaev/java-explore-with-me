package ru.practicum.main_server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.service.CommentService;

import java.util.List;


@RestController
@RequestMapping(path = "/admin")
@Slf4j
@AllArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/users/{userId}/comment/{comId}")
    void deleteComment(@PathVariable Long userId,
                       @PathVariable Long comId) {
        log.info("Delete comment by userId={} and comId{}", userId, comId);
        commentService.deleteComment(userId, comId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(@PathVariable long eventId) {
        log.info("Get event comments Id={}", eventId);
        return commentService.getEventComments(eventId);
    }

}
