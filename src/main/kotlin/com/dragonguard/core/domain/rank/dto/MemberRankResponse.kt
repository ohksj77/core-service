package com.dragonguard.core.domain.rank.dto

import com.dragonguard.core.domain.member.Tier

data class MemberRankResponse(
    val id: Long?,
    val githubId: String?,
    val tier: Tier?,
    val profileImage: String?,
) {
    var contributionAmount: Long? = null
}
