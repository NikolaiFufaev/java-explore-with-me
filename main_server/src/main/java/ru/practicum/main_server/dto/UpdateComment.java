package ru.practicum.main_server.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComment {
    @NonNull
    private Long id;
    private String text;
}
