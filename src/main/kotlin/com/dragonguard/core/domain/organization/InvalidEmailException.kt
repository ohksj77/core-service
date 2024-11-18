package com.dragonguard.core.domain.organization

class InvalidEmailException(
    override val message: String? = "Invalid email"
) : IllegalArgumentException(message)