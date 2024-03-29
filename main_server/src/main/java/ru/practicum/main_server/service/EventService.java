package ru.practicum.main_server.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.client.HitClient;
import ru.practicum.main_server.dto.*;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.RejectedRequestException;
import ru.practicum.main_server.exception.WrongRequestException;
import ru.practicum.main_server.mapper.EventMapper;
import ru.practicum.main_server.model.*;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.ParticipationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class EventService {
    public static final int MIN_HOURS = 2;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final UserService userService;
    private final HitClient hitClient;
    private final CategoryRepository categoryRepository;
    private final LocationService locationService;
    private final EventMapper eventMapper;

    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        LocalDateTime start;
        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        }
        LocalDateTime end;
        if (rangeEnd == null) {
            end = LocalDateTime.now().plusHours(MIN_HOURS);
        } else {
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
        }

        List<Event> events = eventRepository.searchEvents(text, categories, paid, start, end,
                        PageRequest.of(from / size, size))
                .stream()
                .collect(Collectors.toList());
        if (sort.equals("EVENT_DATE")) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDtos = events.stream()
                .filter(event -> event.getState().equals(State.PUBLISHED))
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
        if (sort.equals("VIEWS")) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        if (onlyAvailable) {
            eventShortDtos = eventShortDtos.stream()
                    .filter(eventShortDto -> eventShortDto.getConfirmedRequests()
                            <= checkAndGetEvent(eventShortDto.getId()).getParticipantLimit())
                    .collect(Collectors.toList());
        }
        return eventShortDtos;
    }

    public EventFullDto getEventById(long id) {
        EventFullDto dto = eventMapper.toEventFullDto(checkAndGetEvent(id));
        if (!(dto.getState().equals(State.PUBLISHED.toString()))) {
            throw new WrongRequestException("Wrong state by request");
        }
        return setConfirmedRequestsAndViewsEventFullDto(dto);
    }

    public List<EventShortDto> getEventsCurrentUser(long userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
    }


    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {

        Event event = checkAndGetEvent(updateEventRequest.getEventId());
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("only creator can update event");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new WrongRequestException("you can`t update published event");
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime date = LocalDateTime.parse(updateEventRequest.getEventDate(),
                    DATE_TIME_FORMATTER);
            if (date.isBefore(LocalDateTime.now().minusHours(MIN_HOURS))) {
                throw new WrongRequestException("date event is too late");
            }
            event.setEventDate(date);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getAnnotation() == null || newEventDto.getDescription() == null) {
            throw new RejectedRequestException("annotation or description cannot be null");
        }
        Location location = newEventDto.getLocation();
        log.info("before location save");
        location = locationService.save(location);
        log.info("location save");
        Event event = eventMapper.toNewEvent(newEventDto);
        log.info("event {}", event);
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(MIN_HOURS))) {
            throw new WrongRequestException("date event is too late");
        }
        event.setInitiator(userService.checkAndGetUser(userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        event.setCategory(category);
        event.setLocation(location);

        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public EventFullDto getEventCurrentUser(Long userId, Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("only initiator can get fullEventDto");
        }
        return setConfirmedRequestsAndViewsEventFullDto(eventMapper.toEventFullDto(event));
    }


    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("only initiator can cancel event");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new WrongRequestException("you can cancel only pending event");
        }
        event.setState(State.CANCELED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime start;
        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        }
        LocalDateTime end;
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
        } else {
            end = LocalDateTime.now().plusHours(MIN_HOURS);
        }

        return eventRepository.searchEventsByAdmin(users, states, categories, start, end,
                        PageRequest.of(from / size, size))
                .stream()
                .map(eventMapper::toEventFullDto)
                .map(this::setConfirmedRequestsAndViewsEventFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = checkAndGetEvent(eventId);


        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
            event.setCategory(category);
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            LocalDateTime date = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                    DATE_TIME_FORMATTER);
            if (date.isBefore(LocalDateTime.now().minusHours(MIN_HOURS))) {
                throw new WrongRequestException("date event is too late");
            }
            event.setEventDate(date);
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public EventFullDto publishEventByAdmin(Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(MIN_HOURS))) {
            throw new WrongRequestException("date event is too late");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new WrongRequestException("admin can publish only pending event");
        }
        event.setState(State.PUBLISHED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public EventFullDto rejectEventByAdmin(Long eventId) {
        Event event = checkAndGetEvent(eventId);

        event.setState(State.CANCELED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    public Event checkAndGetEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new ObjectNotFoundException("event with id = " + eventId + " not found"));
    }

    public EventShortDto setConfirmedRequestsAndViewsEventShortDto(EventShortDto eventShortDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventShortDto.getId(), StatusRequest.CONFIRMED);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(getViews(eventShortDto.getId()));
        return eventShortDto;
    }

    public EventFullDto setConfirmedRequestsAndViewsEventFullDto(EventFullDto eventFullDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventFullDto.getId(), StatusRequest.CONFIRMED);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(getViews(eventFullDto.getId()));
        return eventFullDto;
    }

    public int getViews(long eventId) {
        ResponseEntity<Object> responseEntity = hitClient.getStat(
                LocalDateTime.MIN,
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                false);

        if (Objects.requireNonNull(responseEntity.getBody()).equals("")) {
            return (Integer) ((LinkedHashMap<?, ?>) responseEntity.getBody()).get("hits");
        }

        return 0;
    }

    public void sentHitStat(String uri, String remoteAddr) {
        log.info("request URL {}", uri);
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main_server")
                .uri(uri)
                .ip(remoteAddr)
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        hitClient.createHit(endpointHit);
    }
}
