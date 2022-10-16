package de.klamtluk.urlshortener.service.events;

import de.klamtluk.urlshortener.repository.UrlStats;
import de.klamtluk.urlshortener.repository.UrlStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UrlInteractionListener {
    @Autowired
    private UrlStatsRepository urlStatsRepository;

    @Async
    @EventListener
    public void handleUrlOpened(UrlOpenedEvent event) {
        urlStatsRepository.updateClicksById(event.getUrl());
    }

    @Async
    @EventListener
    public void handleUrlCreated(UrlCreatedEvent event) {
        final var urlStats = new UrlStats();
        urlStats.setId(event.getUrl());
        urlStats.setClicks(0);
        urlStatsRepository.save(urlStats);
    }
}
