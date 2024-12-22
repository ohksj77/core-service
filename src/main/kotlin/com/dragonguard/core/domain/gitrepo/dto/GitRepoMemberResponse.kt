package com.dragonguard.core.domain.gitrepo.dto

data class GitRepoMemberResponse(
    val githubId: String?,
    val profileUrl: String?,
    val commits: Int?,
    val additions: Int?,
    val deletions: Int?,
    val isServiceMember: Boolean?,
)
