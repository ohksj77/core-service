package com.dragonguard.core.domain.contribution.dto

import com.dragonguard.core.domain.organization.OrganizationType

class ContributionRequest(
    val memberId: Long,
    val githubId: String,
    val githubToken: String,
    val organizationId: Long?,
    val organizationType: OrganizationType?,
)