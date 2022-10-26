package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
