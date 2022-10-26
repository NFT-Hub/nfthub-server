package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Getter
public class ArtistImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", length = 500)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;

    private boolean isMain;

    public void setArtist(Artist artist) {
        this.artist = artist;
        artist.getArtistImages().add(this);
    }
}
