package com.dragonguard.core.global.exception

class EntityNotFoundException(
    override val message: String,
) : IllegalArgumentException(message) {
    companion object {
        fun member() = EntityNotFoundException("Member not found")
        fun organization() = EntityNotFoundException("Organization not found")
        fun email() = EntityNotFoundException("Email not found")
        fun gitOrg() = EntityNotFoundException("GitOrg not found")
        fun gitRepo() = EntityNotFoundException("GitRepo not found")
    }
}
