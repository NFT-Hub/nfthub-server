package com.nfthub.api.repository

import com.nfthub.api.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

// Temp, to be redis
@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
    fun getRefreshTokenByEmail(email: String): RefreshToken?
}