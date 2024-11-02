package com.dragonguard.core.domain.member.exception

class JwtProcessingException(
    override val message: String,
) : IllegalArgumentException() {
    companion object {
        fun blankToken() = JwtProcessingException("Invalid token")

        fun expiredToken() = JwtProcessingException("Expired token")
    }
}
