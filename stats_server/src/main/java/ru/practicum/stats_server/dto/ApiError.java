package ru.practicum.stats_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    @NotBlank
    private List<Error> errors;
    @NotEmpty
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
