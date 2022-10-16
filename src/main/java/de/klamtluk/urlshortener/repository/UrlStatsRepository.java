package de.klamtluk.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UrlStatsRepository extends JpaRepository<UrlStats, String> {
    @Transactional
    @Modifying
    @Query("update UrlStats u set u.clicks = u.clicks + 1 where u.id = ?1")
    void updateClicksById(String id);
}