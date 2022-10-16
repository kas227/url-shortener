package de.klamtluk.urlshortener.api;

public record GetUrlStatsDto(String url, int clicks) {
}
