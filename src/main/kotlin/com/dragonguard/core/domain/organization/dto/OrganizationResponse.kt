package com.dragonguard.core.domain.organization.dto

import com.dragonguard.core.domain.organization.OrganizationType

data class OrganizationResponse(
    val id: Long?,
    val name: String?,
    val organizationType: OrganizationType?,
    val emailEndpoint: String?,
    val contributionAmount: Int?,
)
