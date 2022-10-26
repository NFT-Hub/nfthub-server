package com.nfthub.core.repository;

import com.nfthub.core.entity.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    Optional<CategoryGroup> findByName(String name);
}
