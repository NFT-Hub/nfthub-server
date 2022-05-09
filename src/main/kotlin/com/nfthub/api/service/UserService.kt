package com.nfthub.api.service

import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.controller.UserEmailExistException
import com.nfthub.api.dto.*
import com.nfthub.api.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw NotFoundException("user not found")
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            arrayListOf()
        )
    }

    fun getUser(email: String) = userRepository.findByEmail(email) ?: throw NotFoundException("user not exist:$email")
    fun getUser(userId: Long) =
        userRepository.findByIdOrNull(userId) ?: throw NotFoundException("user not found $userId")

    fun getUserResponse(userId: Long) = getUser(userId).toResponse()
    fun getUsersResponse(pageable: Pageable) = userRepository.findAll(pageable).map { it.toResponse() }

    @Transactional
    fun createUser(userCreateRequest: UserCreateRequest): UserResponse {
        val user = userRepository.findByEmail(userCreateRequest.email)
        if (user != null) throw UserEmailExistException("user email:${userCreateRequest.email} already exist")
        return userCreateRequest.toEntity()
            .apply {
                passwords = passwordEncoder.encode(userCreateRequest.password)
            }
            .let { userRepository.save(it) }.toResponse()
    }

    @Transactional
    fun updateUser(userId: Long, userUpdateRequest: UserUpdateRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotFoundException("user not exist")
        return user.apply {
            name = userUpdateRequest.name ?: name
        }.toResponse()
    }


}