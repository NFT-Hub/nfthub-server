package com.nfthub.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "auth", description = "인증 api")
@RestController
@RequestMapping("/api/v1/auth")
class UserAuthController {
    @Operation(summary = "유저 로그인", description = "엑세스 토큰, 리프레시 토큰 발급")
    @PostMapping("/login")
    fun login() {
    }

    @Operation(summary = "회원가입", description = "가입 완료한 유저정보 및 엑세스토큰, 리프레시토큰 발급")
    @PostMapping("/register")
    fun register() {
    }

    @Operation(summary = "로그아웃", description = "엑세스 토큰, 리프레시 토큰 무효화")
    @PostMapping("logout")
    fun logout() {
    }

    @Operation(summary = "리프레시 토큰으로 엑세스 토큰 재발급")
    @PostMapping("/reissue")
    fun updateAccessToken() {
    }


}