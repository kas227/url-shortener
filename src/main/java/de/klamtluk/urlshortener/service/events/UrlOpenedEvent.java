package de.klamtluk.urlshortener.service.events;

import org.springframework.context.ApplicationEvent;

public class UrlOpenedEvent extends ApplicationEvent {
    private final String url;

    public UrlOpenedEvent(Object source, String url) {
        super(source);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
