package de.klamtluk.urlshortener.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.klamtluk.urlshortener.repository.UrlStatsRepository;

@RestController
@RequestMapping(path = "/api/stats")
@Validated
public class UrlStatsController {
    @Autowired
    private UrlStatsRepository urlStatsRepository;

    @GetMapping
    public GetUrlStatsDto getUrlStats(@RequestParam(name = "key") @NotEmpty @Pattern(regexp = "^[=a-zA-Z0-9]+",
            message = "Invalid short url. Only alphanumeric values are supported!") String key) {
        final var urlStats = urlStatsRepository.findById(key)
                .orElseThrow();
        return new GetUrlStatsDto(key, urlStats.getClicks());
    }
}
