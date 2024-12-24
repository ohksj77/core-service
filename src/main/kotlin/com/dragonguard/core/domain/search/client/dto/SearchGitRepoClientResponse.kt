package com.dragonguard.core.domain.search.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SearchGitRepoClientResponse(
    val items: List<SearchGitRepoClientResponseItem>,
) {
    companion object {
        data class SearchGitRepoClientResponseItem(
            @JsonProperty("full_name")
            val fullName: String,
            val language: String,
            val description: String,
            @JsonProperty("created_at")
            val createdAt: String,
        )
    }
}
