package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.rank.exception.RankAccessException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class MemberControllerAdvice {
    @ExceptionHandler(RankAccessException::class)
    fun handleRankAccessException(e: RankAccessException): String = e.message
}
