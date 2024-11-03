package com.dragonguard.core.domain.contribution.client.dto

data class ContributionClientRequest(
    val githubId: String,
    val githubToken: String,
    val year: Int,
)
