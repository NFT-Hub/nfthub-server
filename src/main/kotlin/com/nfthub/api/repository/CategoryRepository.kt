package com.nfthub.api.repository

import com.nfthub.api.entity.Category
import com.nfthub.api.entity.CategoryGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Category?
}

@Repository
interface CategoryGroupRepository : JpaRepository<CategoryGroup, Long> {
    fun findByName(name: String): CategoryGroup?
}

