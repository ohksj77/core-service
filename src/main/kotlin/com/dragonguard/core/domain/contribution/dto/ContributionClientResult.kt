package com.dragonguard.core.domain.contribution.dto

data class ContributionClientResult(
    val commit: Int,
    val pullRequest: Int,
    val issue: Int,
    val codeReview: Int,
) {
    fun getTotal(): Int = commit + pullRequest + issue + codeReview
}
