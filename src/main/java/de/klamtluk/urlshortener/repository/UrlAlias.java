package de.klamtluk.urlshortener.repository;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "url_alias")
public class UrlAlias {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "long_url", nullable = false, length = 512)
    private String longUrl;

    @CreatedDate
    private Date createdDate;

    public UrlAlias() {
    }

    public UrlAlias(String url, String alias) {
        this.longUrl = url;
        this.id = alias;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}