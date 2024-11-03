package com.dragonguard.core.domain.search.client.dto

data class SearchMemberClientResponse(
    val items: List<SearchMemberClientResponseItem>,
) {
    companion object {
        data class SearchMemberClientResponseItem(
            val login: String,
        )
    }
}
