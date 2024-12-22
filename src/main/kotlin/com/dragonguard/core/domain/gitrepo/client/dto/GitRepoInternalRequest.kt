package com.dragonguard.core.domain.gitrepo.client.dto

data class GitRepoInternalRequest(
    val gitRepoId: Long,
    val memberId: Long,
    val githubToken: String,
    val name: String,
)
