package com.dragonguard.core.global.advice

import com.dragonguard.core.global.exception.RestClientException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalErrorAdvice {
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(RestClientException::class)
    fun handleRestClientException(e: RestClientException): String = e.message
}
