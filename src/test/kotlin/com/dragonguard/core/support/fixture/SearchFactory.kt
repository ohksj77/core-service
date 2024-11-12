package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.search.client.dto.SearchGitRepoClientResponse
import com.dragonguard.core.domain.search.client.dto.SearchMemberClientResponse
import java.time.LocalDateTime

class SearchFactory {
    companion object {
        fun createSearchMemberResponse(): List<SearchMemberClientResponse.Companion.SearchMemberResponse> =
            listOf(
                SearchMemberClientResponse.Companion.SearchMemberResponse("dragon", true),
                SearchMemberClientResponse.Companion.SearchMemberResponse("dragonguard", true),
                SearchMemberClientResponse.Companion.SearchMemberResponse("dragonball", false),
            )

        fun createSearchGitRepoResponse(): List<SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem> =
            listOf(
                SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem(
                    "dragon",
                    "dragon",
                    "dragon",
                    LocalDateTime.now().toString(),
                ),
                SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem(
                    "dragonguard",
                    "dragonguard",
                    "dragonguard",
                    LocalDateTime.now().toString(),
                ),
                SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem(
                    "dragonball",
                    "dragonball",
                    "dragonball",
                    LocalDateTime.now().toString(),
                ),
            )
    }
}
