package de.klamtluk.urlshortener.api;

import java.util.Date;
import java.util.List;

public class SimpleApiError {
    private final String message;
    private final Date timestamp;

    private List<String> errors;

    public SimpleApiError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public SimpleApiError(String message, List<String> errors) {
        this.message = message;
        this.timestamp = new Date();
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return this.errors;
    }

}
