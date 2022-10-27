package com.nfthub.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Magazine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 200)
    private String description;

    @Column
    private String url;

    @Column
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "magazine", cascade = CascadeType.ALL)
    private List<MagazineTag> magazineTags = new ArrayList<>();

    @OneToMany(mappedBy = "magazine", cascade = CascadeType.ALL)
    private List<MagazineImage> magazineImages = new ArrayList<>();

    public Magazine(String title, String description, String url, String author) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
