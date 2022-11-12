package ru.practicum.main_server.dto;

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
public class UpdateEventRequest {

    @NotNull
    private Long eventId;
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotEmpty
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @Size(max = 30)
    private String eventDate;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    @NotEmpty
    @Size(min = 3, max = 120)
    private String title;
}
