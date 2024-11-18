package com.dragonguard.core.domain.admin.dto

import com.dragonguard.core.domain.organization.OrganizationType

data class AdminOrganizationResponse(
    val id: Long,
    val name: String,
    val type: OrganizationType,
    val emailEndpoint: String,
)