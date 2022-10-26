package com.nfthub.core.entity;

import javax.persistence.*;

@Table
@Entity
public class ArtistTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    public void setArtist(Artist artist) {
        this.artist = artist;
        artist.getArtistTags().add(this);
    }
}
