package ru.practicum.main_server.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.dto.UpdateComment;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.RejectedRequestException;
import ru.practicum.main_server.mapper.CommentMapper;
import ru.practicum.main_server.model.Comment;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.model.State;
import ru.practicum.main_server.model.User;
import ru.practicum.main_server.repository.CommentRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {

        if (commentDto.getText().length() == 0 || commentDto.getText() == null) {
            throw new RejectedRequestException("Sorry comment null text");
        }
        Event event = checkAndGetEvent(eventId);
        if (!event.getState().toString().equals(State.PUBLISHED.toString())) {
            throw new RejectedRequestException("Sorry you no Event no published");
        }
        User user = checkAndGetUser(userId);
        Comment comment = commentMapper.toComment(commentDto, user, event);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() ->
                        new ObjectNotFoundException(String.format("Comment not found id=%s", comId)));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RejectedRequestException("Sorry you no author comment");
        }
        commentRepository.deleteById(comId);
    }

    public CommentDto updateComment(Long userId, Long eventId, UpdateComment updateComment) {
        if (updateComment.getId() == null) {
            throw new RejectedRequestException("Sorry comment null id");
        }
        Comment comment = commentRepository.findById(updateComment.getId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Comment not found id=%s", updateComment.getId())));
        Event event = checkAndGetEvent(eventId);
        if (updateComment.getText().length() == 0 || updateComment.getText() == null) {
            throw new RejectedRequestException("Sorry comment null text");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new RejectedRequestException("Sorry you no Event no published");
        }
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new RejectedRequestException("Sorry you no author comment");
        }
        commentMapper.updateCommentFromUpdateComment(updateComment, comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public List<CommentDto> getEventComments(long id, int from, int size) {
        Event event = checkAndGetEvent(id);
        return commentRepository.findCommentsByEventOrderByCreated(event, PageRequest.of(from / size, size))
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

    }

    public List<CommentDto> getUserComments(long id, int from, int size) {
        User user = checkAndGetUser(id);

        return commentRepository.findCommentsByAuthorOrderByCreated(user, PageRequest.of(from / size, size))
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

    }

    public Event checkAndGetEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("event with id = " + eventId + " not found"));
    }

    public User checkAndGetUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("user with id = " + userId + " not found"));
    }

}
