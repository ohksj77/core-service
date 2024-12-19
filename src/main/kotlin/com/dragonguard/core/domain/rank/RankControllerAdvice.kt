package com.dragonguard.core.domain.rank

import com.dragonguard.core.domain.rank.exception.RankAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RankControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RankAccessException::class)
    fun handleRankAccessException(e: RankAccessException): String = e.message
}
