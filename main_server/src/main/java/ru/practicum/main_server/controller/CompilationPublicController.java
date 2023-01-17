package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
public class CompilationPublicController {

    private final CompilationService compilationService;

    public CompilationPublicController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping()
    public List<CompilationDto> getAllCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        log.info("get compilations pinned {}", pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDto getCompilationById(@Positive  @PathVariable long id) {
        log.info("get compilation id={}", id);
        return compilationService.getCompilationById(id);
    }
}
