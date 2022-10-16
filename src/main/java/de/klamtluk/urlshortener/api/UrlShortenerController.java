package de.klamtluk.urlshortener.api;

import de.klamtluk.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

@RestController
@Validated
public class UrlShortenerController {
    @Autowired
    UrlShortenerService urlShortenerService;

    @PostMapping
    public String createShortUrl(@RequestBody @Valid CreateShortUrlDto url) {
        return urlShortenerService.createShortUrl(url.url());
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

    @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleConstraintViolations(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<String> handleNotFound(Exception ex) {
        return new ResponseEntity<>("404 - Not Found", HttpStatus.NOT_FOUND);
    }
}
