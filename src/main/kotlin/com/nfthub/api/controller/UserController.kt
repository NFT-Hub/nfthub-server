package com.nfthub.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "user", description = "유저 api")
@RequestMapping("/api/v1/users")
@RestController
class UserController {
    @Operation(summary = "유저 단일 조회 ")
    @GetMapping("/{userId}")
    fun getUserResponse(@PathVariable userId: String) {
    }

    @Operation(summary = "유저 리스트 조회 ")
    @GetMapping
    fun getUserResponses() {
    }

    @Operation(summary = "유저 정보 수정")
    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: String) {
    }
}