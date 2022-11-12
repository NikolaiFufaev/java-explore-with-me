package ru.practicum.stats_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stats_server.dto.EndpointHit;
import ru.practicum.stats_server.model.HitModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static HitModel toHitModel(EndpointHit endpointHit) {
        return HitModel.builder()
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(),
                        DATE_TIME_FORMATTER))
                .build();
    }

    public static EndpointHit toEndpointHit(HitModel hitModel) {
        return EndpointHit.builder()
                .id(hitModel.getId())
                .app(hitModel.getApp())
                .ip(hitModel.getIp())
                .uri(hitModel.getUri())
                .timestamp(hitModel.getTimestamp().format(DATE_TIME_FORMATTER))
                .build();
    }
}
