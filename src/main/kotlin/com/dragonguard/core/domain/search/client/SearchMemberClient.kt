package com.dragonguard.core.domain.search.client

import com.dragonguard.core.domain.search.client.dto.SearchMemberClientResponse
import com.dragonguard.core.domain.search.dto.SearchMemberRequest
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class SearchMemberClient(
    private val restClient: RestClient,
) {
    companion object {
        private const val PATH = "search/users?q=%s&per_page=%d&page=%d"
    }

    fun request(
        request: SearchMemberRequest,
        githubToken: String,
    ): SearchMemberClientResponse =
        restClient
            .get()
            .uri(PATH.format(request.q, request.page))
            .headers { it.setBearerAuth(githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(SearchMemberClientResponse::class.java)
            ?: throw RestClientException.searchMember()
}
