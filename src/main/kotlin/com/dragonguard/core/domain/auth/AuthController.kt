package com.dragonguard.core.domain.auth

import com.dragonguard.core.config.security.jwt.JwtToken
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthController(
    private val authService: AuthService,
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("refresh")
    fun refresh(
        @RequestHeader refreshToken: String?,
        @RequestHeader accessToken: String?,
    ): JwtToken = authService.refresh(refreshToken, accessToken)
}
