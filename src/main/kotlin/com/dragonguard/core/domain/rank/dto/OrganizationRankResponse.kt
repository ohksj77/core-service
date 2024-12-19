package com.dragonguard.core.domain.rank.dto

import com.dragonguard.core.domain.organization.OrganizationType

class OrganizationRankResponse(
    val id: Long?,
    val name: String?,
    val type: OrganizationType?,
) {
    var contributionAmount: Long? = null
}
