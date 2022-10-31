package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull
    private Long id;
    @NotNull
    @Size(max = 255)
    private String name;
    @NotNull
    @Size(max = 50)
    private String email;
}
