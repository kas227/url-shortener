package de.klamtluk.urlshortener.api;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

record CreateShortUrlDto(@URL @NotEmpty String url, String alias) {
}
