package ru.practicum.main_server.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_server.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {

    @NotNull
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    long category;
    @Size(min = 20, max = 7000)
    String description;
    @NotNull
    @Size(max = 30)
    String eventDate;
    @NotNull
    Location location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    @Size(max = 20)
    String state;
    @NotNull
    @Size(min = 3, max = 120)
    String title;
}
