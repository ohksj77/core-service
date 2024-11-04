package com.dragonguard.core.domain.gitrepo.client.dto

data class GitRepoClientRequest(
    val githubId: String,
    val githubToken: String,
)
