package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.NewCompilationDto;
import ru.practicum.main_server.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class CompilationAdminController {
    private final CompilationService compilationService;

    public CompilationAdminController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("add compilation");
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@Positive @PathVariable Long compId) {
        log.info("delete compilation {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@Positive @PathVariable Long compId,
                                           @Positive @PathVariable Long eventId) {
        log.info("delete event {} from compilation {}", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@Positive @PathVariable Long compId,
                                      @Positive @PathVariable Long eventId) {
        log.info("add event {} to compilation {}", eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void addCompilationToMainPage(@Positive @PathVariable Long compId) {
        log.info("add compilation {} to main page", compId);
        compilationService.addCompilationToMainPage(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deleteCompilationFromMainPage(@Positive @PathVariable Long compId) {
        log.info("delete  compilation {} from main page", compId);
        compilationService.deleteCompilationFromMainPage(compId);
    }
}
