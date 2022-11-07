package ru.practicum.main_server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.dto.UpdateComment;
import ru.practicum.main_server.model.Comment;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated()
                        .format(DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
                .build();
    }

    public Comment toComment(CommentDto commentDto, User user, Event event) {
        if (commentDto == null) {
            return null;
        }
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }

    public void updateCommentFromUpdateComment(UpdateComment updateComment, Comment comment) {
        if (updateComment.getText() != null) {
            comment.setText(updateComment.getText());
        }
    }
}
