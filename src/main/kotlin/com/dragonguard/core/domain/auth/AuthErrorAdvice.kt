package com.dragonguard.core.domain.auth

import com.dragonguard.core.domain.auth.exception.JwtProcessingException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthErrorAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtProcessingException::class)
    fun handleJwtProcessingException(e: JwtProcessingException): String = e.message
}
