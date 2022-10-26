package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroup categoryGroup;

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
        categoryGroup.getCategories().add(this);
    }
}
