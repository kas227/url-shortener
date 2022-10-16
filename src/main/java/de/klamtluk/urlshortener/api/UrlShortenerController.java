package de.klamtluk.urlshortener.api;

import de.klamtluk.urlshortener.service.UrlShortenerService;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@Validated
public class UrlShortenerController {
    @Autowired
    UrlShortenerService urlShortenerService;

    @PostMapping
    public String createShortUrl(@RequestBody @Valid CreateShortUrlDto url) {
        return urlShortenerService.createShortUrl(url.url());
    }

    @GetMapping
    public String redirect(@RequestParam @URL(message = "Passed url is malformed!") @NotEmpty String url) {
        return "hello world";
    }

    @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleConstraintViolations(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
