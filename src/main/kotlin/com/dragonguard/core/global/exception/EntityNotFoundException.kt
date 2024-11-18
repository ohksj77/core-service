package com.dragonguard.core.global.exception

class EntityNotFoundException(
    message: String,
) : IllegalArgumentException(message) {
    companion object {
        fun member() = EntityNotFoundException("Member not found")
        fun organization() = EntityNotFoundException("Organization not found")
    }
}
