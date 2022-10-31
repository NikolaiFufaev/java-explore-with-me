package ru.practicum.stats_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @NotNull
    private long id;
    @NotEmpty
    @Size(max = 512)
    private String app;
    @Size(max = 512)
    private String uri;
    @Size(max = 30)
    private String ip;
    @NotNull
    private String timestamp;
}
