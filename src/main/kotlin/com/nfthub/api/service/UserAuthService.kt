package com.nfthub.api.service

import com.nfthub.api.auth.JwtTokenProvider
import com.nfthub.api.controller.ForbiddenException
import com.nfthub.api.controller.UnAuthenticatedException
import com.nfthub.api.dto.LoginRequest
import com.nfthub.api.dto.ReissueRequest
import com.nfthub.api.dto.TokenResponse
import com.nfthub.api.entity.RefreshToken
import com.nfthub.api.entity.Role
import com.nfthub.api.entity.User
import com.nfthub.api.repository.RefreshTokenRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserAuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userService: UserService
) {
    fun hasAuthByUserId(userId: Long): Boolean {
        val authenticatedUser = getAuthenticatedUser()
        if (authenticatedUser.role == Role.ADMIN) return true
        if (userId != authenticatedUser.id) throw ForbiddenException("userId ${authenticatedUser.id} cannot access to userId${userId}'s resource")
        return true
    }

    fun hasNormalAuth(): Boolean {
        val authenticatedUser = getAuthenticatedUser()
        if (authenticatedUser.role == Role.ADMIN) return true
        if (authenticatedUser.role != Role.NORMAL) throw ForbiddenException("user is not allowed")
        return true
    }

    fun hasAdminAuth(): Boolean {
        val authenticatedUser = getAuthenticatedUser()
        if (authenticatedUser.role != Role.ADMIN) throw ForbiddenException("can access only admin user")
        return true
    }

    fun getAuthenticatedUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw UnAuthenticatedException("user need authorization")
        println(authentication)
        return userService.getUser(authentication.name)
    }

    @Transactional
    fun login(loginRequest: LoginRequest): TokenResponse {
        val user = userService.getUser(loginRequest.email)
        val authenticationToken = UsernamePasswordAuthenticationToken(
            user, loginRequest.password
        )
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        return TokenResponse(
            jwtTokenProvider.generateAccessToken(authentication),
            createRefreshToken(loginRequest.email).token
        )
    }


    @Transactional
    fun reissue(tokenRequest: ReissueRequest): TokenResponse {
        val authentication =
            jwtTokenProvider.getAuthenticationFromAccessToken(tokenRequest.accessToken).validateRefreshToken(
                tokenRequest.refreshToken
            )
        return TokenResponse(
            authentication.createAccessToken(),
            authentication.createRefreshToken()
        )
    }

    private fun Authentication.createAccessToken(): String =
        jwtTokenProvider.generateAccessToken(this)


    private fun Authentication.createRefreshToken(): String =
        refreshTokenRepository.save(
            RefreshToken(
                this.name,
                token = jwtTokenProvider.generateRefreshToken()
            )
        ).token

    private fun Authentication.validateRefreshToken(refreshToken: String) = apply {
        val prevRefreshToken = refreshTokenRepository.getRefreshTokenByEmail(this.name)
            ?: throw BadCredentialsException("존재하지 않거나 만료된 refreshToken 입니다.")
        if (refreshToken != prevRefreshToken.token) throw BadCredentialsException("적합하지 않은 refreshToken 입니다.")
    }

    private fun createRefreshToken(email: String): RefreshToken {
        val refreshToken = jwtTokenProvider.generateRefreshToken()
        return refreshTokenRepository.save(
            RefreshToken(
                email = email,
                token = refreshToken
            )
        )
    }
}