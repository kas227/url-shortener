package de.klamtluk.urlshortener.api;

import de.klamtluk.urlshortener.service.UrlShortenerService;
import de.klamtluk.urlshortener.service.events.UrlInteractionPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Validated
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private UrlInteractionPublisher urlInteractionPublisher;

    @PostMapping
    public String createShortUrl(@RequestBody @Valid CreateShortUrlDto url) {
        final String key;
        if (url.hasAlias()) {
            key = urlShortenerService.createShortUrl(url.url(), url.alias());
        } else {
            key = urlShortenerService.createShortUrl(url.url());
        }

        urlInteractionPublisher.urlCreated(key);
        return key;
    }

    @GetMapping(path = "/{url}")
    public ResponseEntity<?> redirect(@PathVariable(name = "url") @NotEmpty @Pattern(regexp = "^[=a-zA-Z0-9]+",
            message = "Invalid short url. Only alphanumeric values are supported!") String shortUrl)
            throws URISyntaxException {
        final var longUrl = urlShortenerService.resolveShortUrl(shortUrl);
        final var redirectTarget = new URI(longUrl);

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(redirectTarget)
                .build();
    }
}
