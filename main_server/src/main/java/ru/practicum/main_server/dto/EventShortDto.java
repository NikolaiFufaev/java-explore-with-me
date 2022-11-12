package ru.practicum.main_server.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {

    @NotNull
    Long id;
    @NotNull
    String annotation;
    @NotNull
    CategoryDto category;
    int confirmedRequests;
    @NotNull
    String eventDate;
    @NotNull
    UserShortDto initiator;
    @NotNull
    boolean paid;
    @NotNull
    @Size(max = 512)
    String title;
    int views;
    List<CommentDto> comments = new ArrayList<>();
}
