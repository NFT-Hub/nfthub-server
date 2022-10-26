package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "artist", cascade = {CascadeType.ALL})
    private List<ArtistImage> artistImages = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = {CascadeType.ALL})
    private List<ArtistTag> artistTags = new ArrayList<>();

}
