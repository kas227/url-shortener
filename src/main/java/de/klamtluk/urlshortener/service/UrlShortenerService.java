package de.klamtluk.urlshortener.service;

import de.klamtluk.urlshortener.repository.UrlEntry;
import de.klamtluk.urlshortener.repository.UrlEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);
    private static final String AUTO_GENERATION_PREFIX = "=";

    @Autowired
    private UrlEntryRepository urlRepository;

    @Autowired
    private UrlEncoder urlEncoder;

    /**
     * Create a shorted url for the passed in url and persist it.
     *
     * <p>
     * Auto generated urls are prefixed with {@link #AUTO_GENERATION_PREFIX} in order to be able to distinguish
     * between generated entries and user dictated ones.
     * </p>
     *
     * @param url valid url to be shortened
     * @return shortened url
     */
    public String createShortUrl(String url) {
        logger.info("Creating new short url for url {}", url);

        final var urlEntry = new UrlEntry(url);
        final var savedUrlEntry = urlRepository.save(urlEntry);

        final var shortUrl = urlEncoder.encode(savedUrlEntry.getId());
        final var prefixedUrl = AUTO_GENERATION_PREFIX + shortUrl;

        logger.info("Successfully created new shortened url {} for {}", prefixedUrl, url);
        return prefixedUrl;
    }

    public String resolveShortUrl(String url) {
        final var decodedId = urlEncoder.decode(url);
        return urlRepository
                .findById(decodedId)
                .orElseThrow()
                .getLongUrl();

    }
}
