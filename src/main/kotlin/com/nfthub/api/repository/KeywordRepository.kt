package com.nfthub.api.repository

import com.nfthub.api.entity.Keyword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface KeywordRepository : JpaRepository<Keyword, Long> {
    fun findByName(name: String): Keyword?
}