package de.klamtluk.urlshortener.api;

import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;

@RestController
@Validated
public class UrlShortenerController {
    static final int URL_LENGTH = 12;

    @PostMapping
    public String createShortUrl(@RequestParam @URL String url) {
        return url.substring(0, url.length() - URL_LENGTH);
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
