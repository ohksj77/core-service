package com.dragonguard.core.global.exception

class EntityNotFoundException(
    override val message: String,
) : IllegalArgumentException(message) {
    companion object {
        fun member() = EntityNotFoundException("Member not found")
        fun organization() = EntityNotFoundException("Organization not found")
        fun email(): Throwable = EntityNotFoundException("Email not found")
    }
}
