package com.dragonguard.core.domain.gitrepo.dto

data class GitRepoCompareResponse(
    val first: GitRepoResponse,
    val second: GitRepoResponse
)
