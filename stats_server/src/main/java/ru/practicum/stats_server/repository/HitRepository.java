package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats_server.model.HitModel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<HitModel, Long> {

    List<HitModel> findAllByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

    List<HitModel> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);


}
