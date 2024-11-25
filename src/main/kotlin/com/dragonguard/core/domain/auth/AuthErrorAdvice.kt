package com.dragonguard.core.domain.auth

import com.dragonguard.core.domain.auth.exception.JwtProcessingException
import com.dragonguard.core.global.dto.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthErrorAdvice {

    private val logger = LoggerFactory.getLogger(AuthErrorAdvice::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtProcessingException::class)
    fun handleJwtProcessingException(e: JwtProcessingException): ErrorResponse {
        logger.error("JwtProcessingException: {}", e.message)
        return ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message)
    }
}
