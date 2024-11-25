package com.dragonguard.core.domain.organization.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class OrganizationJoinRequest(
    @field:NotNull
    val organizationId: Long?,
    @field:Email
    val email: String?
)