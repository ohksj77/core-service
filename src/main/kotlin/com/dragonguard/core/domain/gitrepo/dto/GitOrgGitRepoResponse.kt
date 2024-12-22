package com.dragonguard.core.domain.gitrepo.dto

data class GitOrgGitRepoResponse(
    val profileImage: String?,
    val gitRepos: Set<String>?
)
