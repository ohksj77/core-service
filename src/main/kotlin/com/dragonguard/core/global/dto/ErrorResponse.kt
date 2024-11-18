package com.dragonguard.core.global.dto

import java.util.*

class ErrorResponse(
    val code: Int,
    val message: String,
    val data: ErrorResponseData = ErrorResponseData(UUID.randomUUID()),
) {
    companion object {
        data class ErrorResponseData(
            val errorId: UUID
        )
    }
}
