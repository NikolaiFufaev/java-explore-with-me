package ru.practicum.main_server.exception;

public class RejectedRequestException extends RuntimeException {
    public RejectedRequestException(String message) {
        super(message);
    }
}
