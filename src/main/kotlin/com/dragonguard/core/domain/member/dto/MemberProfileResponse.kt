package com.dragonguard.core.domain.member.dto

import com.dragonguard.core.domain.member.AuthStep
import com.dragonguard.core.domain.member.Tier

data class MemberProfileResponse(
    val name: String,
    val githubId: String,
    val commits: Int,
    val issues: Int,
    val pullRequests: Int,
    val reviews: Int,
    val tier: Tier,
    val authStep: AuthStep,
    val profileImage: String,
    val rank: Int,
    val contributionAmount: Int,
    val organization: String,
    val organizationRank: Int,
    val isLast: Boolean,
    val memberGithubIds: List<String>,
)
