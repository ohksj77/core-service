package com.dragonguard.core.global.dto

class ErrorResponse private constructor(
    val status: String,
    val message: String,
) {
    constructor(message: String) : this(ERROR, message)

    companion object {
        private const val ERROR = "error"
    }
}
