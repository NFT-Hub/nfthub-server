package com.nfthub.core.repository;

import com.nfthub.core.entity.BookmarkMagazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkMagazineRepository extends JpaRepository<BookmarkMagazine, Long> {
}
