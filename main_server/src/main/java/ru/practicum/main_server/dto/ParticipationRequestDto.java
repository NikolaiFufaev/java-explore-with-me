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
public class ParticipationRequestDto {

    @NotEmpty
    private Long id;
    @NotNull
    private String created;
    @NotNull
    private Long event;
    @NotNull
    private Long requester;
    @NotNull
    @Size(max = 20)
    private String status;
}
