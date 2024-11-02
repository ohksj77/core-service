package com.dragonguard.core.global.dto

class SuccessResponse<T> private constructor(
    val status: String,
    val data: T,
) {
    constructor(data: T) : this(SUCCESS, data)

    companion object {
        private const val SUCCESS = "success"
    }
}
