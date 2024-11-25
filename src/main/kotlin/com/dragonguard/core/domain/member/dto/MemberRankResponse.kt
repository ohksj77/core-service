package com.dragonguard.core.domain.member.dto

import com.dragonguard.core.domain.member.Tier

class MemberRankResponse(
    val githubId: String?,
    val contributions: Long?,
    val tier: Tier?,
    val profileImage: String?,
)
