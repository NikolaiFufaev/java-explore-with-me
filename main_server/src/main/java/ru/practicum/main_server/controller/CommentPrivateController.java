package ru.practicum.main_server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.dto.UpdateComment;
import ru.practicum.main_server.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
@AllArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comment")
    CommentDto createComment(@PathVariable Long userId,
                             @Positive @PathVariable Long eventId,
                             @Valid @RequestBody CommentDto commentDto) {
        log.info("add comment by userId={} and eventId{}", userId, eventId);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/comment/{comId}")
    void deleteComment(@Positive  @PathVariable Long userId,
                       @Positive @PathVariable Long comId) {
        log.info("delete comment by userId={} and comId{}", userId, comId);
        commentService.deleteComment(userId, comId);
    }

    @PatchMapping("/events/{eventId}/comment")
    CommentDto updateComment(@Positive @PathVariable Long userId,
                             @Positive @PathVariable Long eventId,
                             @Valid @RequestBody UpdateComment updateComment) {
        log.info("update comment by userId={} and eventId{}", userId, eventId);
        return commentService.updateComment(userId, eventId, updateComment);
    }
}
