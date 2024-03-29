package ru.practicum.stats_server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_server.mapper.HitMapper;
import ru.practicum.stats_server.dto.EndpointHit;
import ru.practicum.stats_server.dto.ViewStats;
import ru.practicum.stats_server.model.HitModel;
import ru.practicum.stats_server.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HitService {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final HitRepository hitRepository;


    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Transactional
    public EndpointHit createHit(EndpointHit endpointHit) {
        HitModel hitModel = HitMapper.toHitModel(endpointHit);
        return HitMapper.toEndpointHit(hitRepository.save(hitModel));
    }

    public List<ViewStats> getViewStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(
                start, DATE_TIME_FORMATTER
        );
        LocalDateTime endDate = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
        List<ViewStats> viewStatsList = new ArrayList<>();
        if (uris == null) {
            uris = hitRepository.findAllByTimestampBetween(startDate, endDate)
                    .stream()
                    .map(HitModel::getUri)
                    .distinct()
                    .collect(Collectors.toList());
        }
        for (String uri : uris) {
            List<HitModel> models = hitRepository.findAllByUriAndTimestampBetween(uri, startDate, endDate);
            if (unique) {
                models = models
                        .stream()
                        .filter(distinctByKey(HitModel::getIp))
                        .collect(Collectors.toList());
            }
            if (!models.isEmpty()) {
                viewStatsList.add(ViewStats.builder()
                        .app(models.get(0).getApp())
                        .uri(uri)
                        .hits(models.size())
                        .build());
            }
        }
        return viewStatsList;
    }
}
