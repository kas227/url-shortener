package de.klamtluk.urlshortener.api;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

record CreateShortUrlDto(@URL(message = "Passed url is not a valid URL.") @NotEmpty String url,
                         @Pattern(regexp = "^[a-zA-Z0-9]*", message = "Passed alias must be an alphanumeric string.") String alias) {
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }
}
