package tech.carrotly.restapi.model.helpers;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Supplier;

public enum Error implements Serializable {
    INVALID_USER_ID("Invalid id", 1),
    INVALID_USER_EMAIL("Invalid email", 2),
    INVALID_USER("Invalid user", 3);

    private final String reason;
    private final Integer code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    Error(String reason, Integer code) {
        this.reason = reason;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return this.name();
    }

    public Supplier<Exception> throw_() {
        return () -> new RuntimeException(this.toString());
    }
}

