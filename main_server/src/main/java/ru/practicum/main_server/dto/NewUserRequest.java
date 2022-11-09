package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotNull
    @Size(max = 50)
    private String name;
    @Email
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String email;
}
