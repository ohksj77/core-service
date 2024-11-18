package com.dragonguard.core.domain.email.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class EmailCheckRequest(
    @field:Min(10000)
    @field:Max(99999)
    var code: Int? = null,
    @field:NotNull
    var organizationId: Long? = null,
)
