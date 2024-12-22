package com.dragonguard.core.domain.gitrepo.client

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientRequest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GitRepoIssueClient(
    private val openApiRestClient: RestClient,
) {
    companion object {
        private const val PATH = "repos/%s/issues?state=closed&per_page=100&page=%d"
        private const val START_PAGE = 1
        private const val MAX_TRY = 10
    }

    fun request(request: GitRepoDetailsClientRequest): Int =
        (START_PAGE..MAX_TRY)
            .asSequence()
            .map { requestOne(request, it) }
            .takeWhile { it != null }
            .filterNotNull()
            .flatMap { it }
            .distinct()
            .count()

    private fun requestOne(
        request: GitRepoDetailsClientRequest,
        page: Int,
    ): List<String>? =
        openApiRestClient
            .get()
            .uri(PATH.format(request.name, page))
            .headers { it.setBearerAuth(request.githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(object : ParameterizedTypeReference<List<String>>() {})
}
