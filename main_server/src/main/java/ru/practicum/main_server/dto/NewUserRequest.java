package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotEmpty
    @Size(max = 255)
    private String name;
    @NotEmpty
    @Size(max = 50)
    private String email;
}
