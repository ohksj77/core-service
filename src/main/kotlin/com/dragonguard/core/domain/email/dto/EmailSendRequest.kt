package com.dragonguard.core.domain.email.dto

data class EmailSendRequest(
    val email: String,
    val code: Int
)