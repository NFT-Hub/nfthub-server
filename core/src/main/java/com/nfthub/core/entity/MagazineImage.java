package com.nfthub.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
public class MagazineImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", length = 500)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private Magazine magazine;

    private boolean isMain;

    public MagazineImage(String url, boolean isMain) {
        this.url = url;
        this.isMain = isMain;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
        magazine.getMagazineImages().add(this);
    }
}
