package com.dragonguard.core.domain.gitrepo.client

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoSparkLineResponse
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GitRepoSparkLineClient(
    private val openApiRestClient: RestClient,
) {
    companion object {
        private const val PATH = "repos/%s/stats/participation"
    }

    fun request(request: GitRepoDetailsClientRequest): GitRepoSparkLineResponse =
        openApiRestClient
            .get()
            .uri(PATH.format(request.name))
            .headers { it.setBearerAuth(request.githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(GitRepoSparkLineResponse::class.java)
            ?: throw RestClientException.gitRepoSparkLine()
}
