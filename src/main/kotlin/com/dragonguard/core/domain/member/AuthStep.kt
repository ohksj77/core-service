package com.dragonguard.core.domain.member

enum class AuthStep {
    NONE,
    GITHUB,
    EMAIL,
    ;

    fun isInitialized(): Boolean {
        return this != NONE
    }
}
