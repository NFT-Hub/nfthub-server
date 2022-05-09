package com.nfthub.api.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JwtTokenProvider(@Value("\${jwt.secret}") secretKey: String) {

    companion object {

        private const val ROLES = "roles"
        private const val ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 // 60분
        private const val REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3 // 3일
    }

    private val key: Key = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(secretKey)
    )
    private val signatureAlgorithm = SignatureAlgorithm.HS256

    fun generateAccessToken(authentication: Authentication): String {
        val accessTokenExpiresIn = Date(Date().time + ACCESS_TOKEN_EXPIRE_TIME)
        return Jwts.builder()
            .setSubject(authentication.name) // payload "sub": "name"
            .claim(ROLES, authentication.authorities.joinToString(",") { it.authority }) // payload "roles": "ROLE_USER"
            .setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022
            .signWith(key, signatureAlgorithm) // header "alg": "HS256"
            .compact()
    }

    fun generateRefreshToken(): String {
        val refreshTokenExpiresIn = Date(Date().time + REFRESH_TOKEN_EXPIRE_TIME)
        return Jwts.builder()
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, signatureAlgorithm)
            .compact()
    }

    fun getAuthenticationFromAccessToken(accessToken: String): Authentication {
        // 토큰 복호화
        val claims = parseClaims(accessToken)

        val authorities: Collection<GrantedAuthority> = arrayListOf()
        val principal: UserDetails = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validateToken(token: String): Boolean {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token) // 적합하지 않으면 여기서 아래의 예외 발생
        return true
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}