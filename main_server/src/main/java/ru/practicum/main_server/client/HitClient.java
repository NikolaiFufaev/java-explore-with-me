package ru.practicum.main_server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.main_server.dto.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class HitClient extends BaseClient {

    public static final String STATISTIC_URL = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public HitClient(@Value("${STAT_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void createHit(EndpointHit endpointHit) {
        post(endpointHit);
    }

    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end,
                                          List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMATTER),
                "end", end.format(DATE_TIME_FORMATTER),
                "uris", uris.get(0),
                "unique", unique
        );
        return get(STATISTIC_URL, parameters);
    }
}
