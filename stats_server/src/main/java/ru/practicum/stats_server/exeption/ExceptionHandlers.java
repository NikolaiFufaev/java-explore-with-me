package ru.practicum.stats_server.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.stats_server.dto.ApiError;

import java.time.LocalDateTime;

public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Throwable exception) {
        return ApiError.builder()
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .reason("object not found in db")
                .message(exception.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
