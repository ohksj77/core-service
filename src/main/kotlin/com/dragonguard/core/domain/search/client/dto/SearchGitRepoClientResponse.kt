package com.dragonguard.core.domain.search.client.dto

data class SearchGitRepoClientResponse(
    val items: List<SearchGitRepoClientResponseItem>,
) {
    companion object {
        data class SearchGitRepoClientResponseItem(
            val fullName: String,
            val language: String,
            val description: String,
            val createdAt: String,
        )
    }
}
