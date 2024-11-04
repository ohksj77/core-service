package com.dragonguard.core.domain.gitrepo.client.dto

data class GitRepoDetailsClientRequest(
    val name: String,
    val githubToken: String,
)
