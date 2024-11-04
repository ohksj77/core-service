package com.dragonguard.core.domain.gitrepo.client.dto

data class GitRepoDetailsClientResponse(
    val fullName: String,
    val forksCount: Int,
    val stargazersCount: Int,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val closedIssuesCount: Int,
    val subscribersCount: Int,
)
