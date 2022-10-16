package de.klamtluk.urlshortener.service;

import de.klamtluk.urlshortener.repository.UrlAlias;
import de.klamtluk.urlshortener.repository.UrlAliasRepository;
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
    private UrlAliasRepository urlAliasRepository;

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

    /**
     * Create a shorted url for the passed in url and alias and persist it.
     *
     * @param url   valid url to be shortened
     * @param alias key to be set for url
     * @return alias
     */
    public String createShortUrl(String url, String alias) {
        if (alias.startsWith(AUTO_GENERATION_PREFIX)) {
            throw new IllegalArgumentException("Alias cannot start with " + AUTO_GENERATION_PREFIX);
        }

        logger.info("Creating new alias {} for url {}", alias, url);

        final var aliasEntry = new UrlAlias(url, alias);
        urlAliasRepository.save(aliasEntry);

        return alias;
    }

    public String resolveShortUrl(String url) {
        if (isGenerated(url)) {
            return resolveGeneratedUrl(url);
        } else {
            return resolveAliasUrl(url);
        }
    }

    private String resolveAliasUrl(String url) {
        return urlAliasRepository.findById(url).orElseThrow().getLongUrl();

    }

    private String resolveGeneratedUrl(String url) {
        final var unprefixedUrl = url.substring(1);
        final var decodedId = urlEncoder.decode(unprefixedUrl);
        return urlRepository
                .findById(decodedId)
                .orElseThrow()
                .getLongUrl();
    }

    private boolean isGenerated(String url) {
        return url.startsWith(AUTO_GENERATION_PREFIX);
    }
}
