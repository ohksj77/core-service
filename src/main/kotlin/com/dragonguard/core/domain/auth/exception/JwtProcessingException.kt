package com.dragonguard.core.domain.auth.exception

class JwtProcessingException(
    override val message: String,
) : IllegalArgumentException() {
    companion object {
        fun blankToken() = JwtProcessingException("Invalid token")

        fun expiredToken() = JwtProcessingException("Expired token")
    }
}
