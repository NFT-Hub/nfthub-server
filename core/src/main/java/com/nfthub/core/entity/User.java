package com.nfthub.core.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean isVerified = false;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<BookmarkMagazine> bookmarkMagazines = new ArrayList<>();
}
