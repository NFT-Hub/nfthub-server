package com.nfthub.core.entity;

import javax.persistence.*;

@Entity
@Table
public class BookmarkArt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
