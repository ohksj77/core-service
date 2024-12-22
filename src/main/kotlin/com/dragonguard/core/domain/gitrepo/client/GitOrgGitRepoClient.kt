package com.dragonguard.core.domain.gitrepo.client

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientResponse
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GitOrgGitRepoClient(
    private val openApiRestClient: RestClient,
) {
    companion object {
        private const val PATH = "orgs/%s/repos"
    }

    fun request(request: GitRepoClientRequest): List<GitRepoClientResponse> =
        openApiRestClient
            .get()
            .uri(PATH.format(request.githubId))
            .headers { it.setBearerAuth(request.githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(object : ParameterizedTypeReference<List<GitRepoClientResponse>>() {})
            ?: throw RestClientException.gitOrgGitRepo()
}
