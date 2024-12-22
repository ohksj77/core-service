package com.dragonguard.core.domain.gitrepo.dto

data class GitRepoResponse(
    val sparkLine: List<Int>?,
    val gitRepoMembers: List<GitRepoMemberResponse>?,
)
