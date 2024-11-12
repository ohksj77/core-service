package com.dragonguard.core.domain.member

enum class AuthStep {
    GITHUB,
    KLIP,
    EMAIL,
    ;

    companion object {
        fun highestAuthStep(authSteps: Set<AuthStep>): AuthStep = entries.toTypedArray().findLast { it in authSteps } ?: GITHUB
    }
}
