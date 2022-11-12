package ru.practicum.main_server.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_server.model.Comment;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.model.User;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
     List<Comment> findAllByEventOrderByCreated(Event event);
     Page<Comment> findCommentsByAuthorOrderByCreated(User user, Pageable pageable);
     Page<Comment> findCommentsByEventOrderByCreated(Event event, Pageable pageable);
}
