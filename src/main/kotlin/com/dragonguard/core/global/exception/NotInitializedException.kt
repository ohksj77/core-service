package com.dragonguard.core.global.exception

class NotInitializedException(
    override val message: String,
) : IllegalStateException(message) {
    companion object {
        fun memberGithubToken() = NotInitializedException("Github token is not initialized")

        fun entityId() = NotInitializedException("Entity id is not initialized")
    }
}
