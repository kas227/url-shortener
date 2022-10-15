package de.klamtluk.urlshortener.api;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "url-shortener")
@Validated
public class UrlShortenerController {
    static final int URL_LENGTH = 12;

    @PostMapping
    public String shortUrl(@RequestParam @URL String url){
        return url.substring(0, url.length() - URL_LENGTH);
    }
}
