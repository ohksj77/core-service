package com.dragonguard.core.domain.admin.dto

import com.dragonguard.core.domain.organization.OrganizationStatus
import jakarta.validation.constraints.NotNull

data class AdminDecideRequest(
    @field:NotNull
    val organizationStatus: OrganizationStatus,
)