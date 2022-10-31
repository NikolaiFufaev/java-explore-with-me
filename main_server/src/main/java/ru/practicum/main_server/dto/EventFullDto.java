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
public class EventFullDto {

    @NotNull
    private Long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    private Location location;
    @NotNull
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    @Size(max = 20)
    private String state;
    @NotNull
    @Size(max = 512)
    private String title;
    private int views;
}
