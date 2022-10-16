package de.klamtluk.urlshortener.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UrlInteractionPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void urlOpened(String url) {
        UrlOpenedEvent event = new UrlOpenedEvent(this, url);
        applicationEventPublisher.publishEvent(event);
    }

    public void urlCreated(String key) {
        final var event = new UrlCreatedEvent(this, key);
        applicationEventPublisher.publishEvent(event);
    }

}
