package com.dragonguard.core.domain.search.client.dto

import com.dragonguard.core.domain.search.dto.ServiceMembers

data class SearchMemberClientResponse(
    val items: List<SearchMemberClientResponseItem>?,
) {
    fun toSearchMemberResponse(serviceMembers: ServiceMembers): List<SearchMemberResponse> =
        items!!.map {
            SearchMemberResponse(
                it.login!!,
                serviceMembers.isServiceMember(it.login),
            )
        }

    companion object {
        data class SearchMemberClientResponseItem(
            val login: String?,
        )

        data class SearchMemberResponse(
            val githubId: String,
            val isServiceMember: Boolean,
        )
    }
}
