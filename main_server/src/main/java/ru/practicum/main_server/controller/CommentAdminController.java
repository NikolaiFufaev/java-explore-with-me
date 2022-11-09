package ru.practicum.main_server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.service.CommentService;

import javax.validation.constraints.Positive;


@RestController
@Slf4j
@AllArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/admin/users/{userId}/comment/{comId}")
    void deleteComment(@Positive @PathVariable Long userId,
                       @Positive @PathVariable Long comId) {
        log.info("Delete comment by userId={} and comId{}", userId, comId);
        commentService.deleteComment(userId, comId);
    }
}
