package de.klamtluk.urlshortener.repository;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "url_entry")
public class UrlEntry {

    public UrlEntry() {
    }

    public UrlEntry(String longUrl) {
        this.longUrl = longUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_entry_seq")
    @SequenceGenerator(name = "url_entry_seq", initialValue = 1000)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "long_url", nullable = false, length = 512)
    private String longUrl;

    @CreatedDate
    private Date createdDate;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}