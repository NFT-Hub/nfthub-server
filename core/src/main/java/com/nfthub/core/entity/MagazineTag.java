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

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setMagazine(Magazine magazine) {
        if (this.magazine != null) {
            this.magazine.getMagazineTags().remove(this);
        }
        this.magazine = magazine;
        this.magazine.getMagazineTags().add(this);
    }
}
