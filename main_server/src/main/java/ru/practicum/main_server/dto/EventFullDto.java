package ru.practicum.main_server.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_server.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    @NotNull
    Long id;
    @NotNull
    String annotation;
    @NotNull
    CategoryDto category;
    int confirmedRequests;
    String createdOn;
    String description;
    @NotNull
    String eventDate;
    @NotNull
    UserShortDto initiator;
    Location location;
    @NotNull
    boolean paid;
    int participantLimit;
    String publishedOn;
    boolean requestModeration;
    @Size(max = 20)
    String state;
    @NotNull
    @Size(min = 3, max = 120)
    String title;
    int views;
    List<CommentDto> comments;
}
