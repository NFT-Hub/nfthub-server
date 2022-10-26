package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Getter
public class ArtTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Art art;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    public void setArt(Art art) {
        this.art = art;
        art.getArtTags().add(this);
    }
}
