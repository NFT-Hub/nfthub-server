package com.nfthub.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
public class MagazineTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Magazine magazine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    public MagazineTag(Tag tag) {
        this.tag = tag;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
        magazine.getMagazineTags().add(this);
    }
}
