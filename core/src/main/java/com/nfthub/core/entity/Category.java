package com.nfthub.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryGroup categoryGroup;

    public Category(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryGroup(CategoryGroup categoryGroup) {
        if (this.categoryGroup != null) {
            this.categoryGroup.getCategories().remove(this);
        }
        this.categoryGroup = categoryGroup;
        categoryGroup.getCategories().add(this);
    }
}
