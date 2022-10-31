package ru.practicum.main_server.model;

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
public class ViewStats {

    @NotEmpty
    @Size(max = 512)
    private String app;
    @Size(max = 512)
    private String uri;
    private int hits;
}
