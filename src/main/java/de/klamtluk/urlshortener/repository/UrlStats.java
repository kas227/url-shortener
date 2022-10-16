package de.klamtluk.urlshortener.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "url_stats")
public class UrlStats {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "clicks")
    private Integer clicks;

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}