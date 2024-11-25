package com.dragonguard.core.domain.member.dto

data class MemberDetailsResponse(
    val commits: Int?,
    val issues: Int?,
    val pullRequests: Int?,
    val codeReviews: Int?,
    val profileImage: String?,
    val gitRepos: List<String>,
    val organization: String?,
    val rank: Int?,
)