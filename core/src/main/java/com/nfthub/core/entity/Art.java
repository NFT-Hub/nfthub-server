package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
public class Art {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Art art;

    @OneToMany(mappedBy = "art", cascade = {CascadeType.ALL})
    private List<ArtImage> artImages = new ArrayList<>();

    @OneToMany(mappedBy = "art", cascade = {CascadeType.ALL})
    private List<ArtTag> artTags = new ArrayList<>();
}
