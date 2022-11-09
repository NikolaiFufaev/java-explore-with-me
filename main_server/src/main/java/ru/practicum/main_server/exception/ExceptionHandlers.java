package ru.practicum.main_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main_server.dto.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(ObjectNotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("object not found")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(WrongRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError forbidden(WrongRequestException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("object not found")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(ServerError e) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("server's exception")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(RejectedRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequest(RejectedRequestException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("required values not found")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
