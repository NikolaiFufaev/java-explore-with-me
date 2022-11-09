package ru.practicum.main_server.service;

import lombok.AllArgsConstructor;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {

        if (commentDto.getText().length() == 0 || commentDto.getText() == null) {
            throw new RejectedRequestException("Sorry comment null text");
        }
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getState().toString().equals(State.PUBLISHED.toString())) {
            throw new RejectedRequestException("Sorry you no Event no published");
        }
        User user = userService.findById(userId);
        Comment comment = commentMapper.toComment(commentDto, user, event);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long comId) {
        Optional<Comment> byId = commentRepository.findById(comId);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Comment not found id=%s", comId));
        }
        if (!Objects.equals(byId.get().getAuthor().getId(), userId)) {
            throw new RejectedRequestException("Sorry you no author comment");
        }
        commentRepository.deleteById(comId);
    }

    public CommentDto updateComment(Long userId, Long eventId, UpdateComment updateComment) {
        if (updateComment.getId() == null) {
            throw new RejectedRequestException("Sorry comment null id");
        }
        Optional<Comment> commentOptional = commentRepository.findById(updateComment.getId());
        if (commentOptional.isEmpty()) {
            throw new ObjectNotFoundException(String.format("Comment not found id=%s", updateComment.getId()));
        }
        if (updateComment.getText().length() == 0 || updateComment.getText() == null) {
            throw new RejectedRequestException("Sorry comment null text");
        }
        if (!eventRepository.findById(eventId).get().getState().equals(State.PUBLISHED)) {
            throw new RejectedRequestException("Sorry you no Event no published");
        }
        if (!Objects.equals(commentOptional.get().getAuthor().getId(), userId)) {
            throw new RejectedRequestException("Sorry you no author comment");
        }
        Comment comment = commentOptional.get();
        commentMapper.updateCommentFromUpdateComment(updateComment, comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public List<CommentDto> getEventComments(long id) {
        Optional<Event> event = eventRepository.findById(id);
        return commentRepository.findAllByEventOrderByCreated(event.get())
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

    }

    public List<CommentDto> getUserComments(long id) {
        User user = userService.findById(id);

        return commentRepository.findCommentsByAuthorOrderByCreated(user)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

    }

}
