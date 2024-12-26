package com.dragonguard.core.domain.gitrepo.dto

data class GitRepoCompareResponse(
    val first: GitRepoDetailsResponse,
    val second: GitRepoDetailsResponse,
)
