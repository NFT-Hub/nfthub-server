package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Getter
public class ArtImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", length = 500)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private Art art;

    private boolean isMain;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setArt(Art art) {
        this.art = art;
        art.getArtImages().add(this);
    }

    public void setMain(boolean main) {
        isMain = main;
    }
}