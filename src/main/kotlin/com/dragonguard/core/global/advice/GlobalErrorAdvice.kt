package com.dragonguard.core.global.advice

import com.dragonguard.core.global.dto.ErrorResponse
import com.dragonguard.core.global.exception.EntityNotFoundException
import com.dragonguard.core.global.exception.NotInitializedException
import com.dragonguard.core.global.exception.RestClientException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalErrorAdvice {

    private val logger = LoggerFactory.getLogger(GlobalErrorAdvice::class.java)

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(RestClientException::class)
    fun handleRestClientException(e: RestClientException): ErrorResponse {
        logger.error("RestClientException: {}", e.message)
        return ErrorResponse(HttpStatus.BAD_GATEWAY.value(), e.message)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotInitializedException::class)
    fun handleNotInitializedException(e: NotInitializedException): ErrorResponse {
        logger.error("NotInitializedException: {}", e.message)
        return ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ErrorResponse {
        logger.error("MethodArgumentNotValidException: {}", e.message)
        return ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ErrorResponse {
        logger.error("EntityNotFoundException: {}", e.message)
        return ErrorResponse(HttpStatus.NOT_FOUND.value(), e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ErrorResponse {
        logger.error("Exception: {}", e.message)
        return ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message ?: "Unknown error")
    }
}
