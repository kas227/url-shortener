package de.klamtluk.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlAliasRepository extends JpaRepository<UrlAlias, String> {
}