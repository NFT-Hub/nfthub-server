package com.nfthub.core.entity;

import javax.persistence.*;

@Entity
@Table
public class BookmarkMagazine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Magazine magazine;

    public void setUser(User user) {
        this.user = user;
        user.getBookmarkMagazines().add(this);
    }
}
