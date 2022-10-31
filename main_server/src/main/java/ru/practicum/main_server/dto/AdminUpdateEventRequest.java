package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_server.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {

    @NotNull
    @Size(max = 1000)
    private String annotation;
    @NotNull
    private Long category;
    @Size(max = 1000)
    private String description;
    @NotNull
    private String eventDate;
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @Size(max = 512)
    private String title;
}
