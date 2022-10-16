package de.klamtluk.urlshortener.service.events;

import org.springframework.context.ApplicationEvent;

public class UrlCreatedEvent extends ApplicationEvent {
    private final String key;

    public UrlCreatedEvent(Object source, String key) {
        super(source);
        this.key = key;
    }

    public String getUrl() {
        return key;
    }
}
