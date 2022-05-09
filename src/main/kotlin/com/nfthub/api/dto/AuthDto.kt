package com.nfthub.api.dto


import com.nfthub.api.EMPTY_STRING
import com.nfthub.api.NOT_BE_EMPTY
import javax.validation.constraints.NotBlank

data class TokenResponse(
    var accessToken: String = EMPTY_STRING,
    var refreshToken: String = EMPTY_STRING,
)

data class LoginRequest(
    @field:NotBlank(message = NOT_BE_EMPTY)
    var email: String,
    @field:NotBlank(message = NOT_BE_EMPTY)
    var password: String
)

data class ReissueRequest(
    @field:NotBlank(message = NOT_BE_EMPTY)
    var accessToken: String,
    @field:NotBlank(message = NOT_BE_EMPTY)
    var refreshToken: String,

    )
