package com.nfthub.api.dto

import com.nfthub.api.EMPTY_STRING
import com.nfthub.api.NOT_BE_EMPTY
import com.nfthub.api.RequestMapper
import com.nfthub.api.ResponseMapper
import com.nfthub.api.entity.Role
import com.nfthub.api.entity.User
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class UserCreateRequest(
    var name: String = EMPTY_STRING,
    @field:NotBlank(message = NOT_BE_EMPTY)
    var email: String = EMPTY_STRING,
    @field:NotBlank(message = NOT_BE_EMPTY)
    var password: String = EMPTY_STRING,
)

data class UserUpdateRequest(
    var name: String? = null,
    var mobileNumber: String? = null
)


data class UserResponse(
    var id: Long = 0,
    var name: String = EMPTY_STRING,
    var email: String = EMPTY_STRING,
    var verifiedEmail: Boolean = false,
    var role: String = Role.NORMAL.toString(),
    var createDate: LocalDateTime? = null,
)


@Mapper
interface UserMapper : RequestMapper<UserCreateRequest, User>, ResponseMapper<UserResponse, User> {
    companion object {
        val INSTANCE: UserMapper = Mappers.getMapper(UserMapper::class.java)
    }
}

fun User.toResponse() = UserMapper.INSTANCE.fromEntity(this)
fun UserCreateRequest.toEntity() = UserMapper.INSTANCE.toEntity(this)
