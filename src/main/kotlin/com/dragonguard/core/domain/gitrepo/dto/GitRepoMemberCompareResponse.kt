package com.dragonguard.core.domain.gitrepo.dto

data class GitRepoMemberCompareResponse(
    val first: List<GitRepoMemberResponse>?,
    val second: List<GitRepoMemberResponse>?,
)
