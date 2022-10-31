package ru.practicum.main_server.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewCompilationDto;
import ru.practicum.main_server.mapper.CompilationMapper;
import ru.practicum.main_server.model.Compilation;
import ru.practicum.main_server.model.Event;
import ru.practicum.main_server.repository.CompilationRepository;
import ru.practicum.main_server.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;


    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .map(this::setViewsAndConfirmedRequestsInDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(long id) {
        CompilationDto compilationDto = CompilationMapper
                .toCompilationDto(compilationRepository
                        .getReferenceById(id));
        return setViewsAndConfirmedRequestsInDto(compilationDto);
    }

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Set<Event> events = newCompilationDto.getEvents()
                .stream()
                .map(eventRepository::getReferenceById)
                .collect(Collectors.toSet());
        compilation.setEvents(events);
        Compilation newCompilation = compilationRepository.saveAndFlush(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(newCompilation);
        return setViewsAndConfirmedRequestsInDto(compilationDto);
    }


    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilationRepository.delete(compilation);
    }

    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        Set<Event> events = compilation.getEvents();
        events.remove(eventRepository.getReferenceById(eventId));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        Set<Event> events = compilation.getEvents();
        events.add(eventRepository.getReferenceById(eventId));
        compilationRepository.save(compilation);
    }

    public void deleteCompilationFromMainPage(Long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    public void addCompilationToMainPage(Long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    private CompilationDto setViewsAndConfirmedRequestsInDto(CompilationDto compilationDto) {
        List<EventShortDto> eventShortDtos = compilationDto.getEvents()
                .stream()
                .map(eventService::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }
}
