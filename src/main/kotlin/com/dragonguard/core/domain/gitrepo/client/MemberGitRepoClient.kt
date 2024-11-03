package com.dragonguard.core.domain.gitrepo.client

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientResponse
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class MemberGitRepoClient(
    private val restClient: RestClient,
) {
    companion object {
        private const val PATH = "users/%s/repos?per_page=100"
    }

    fun request(request: GitRepoClientRequest): List<GitRepoClientResponse> =
        restClient
            .get()
            .uri(PATH.format(request.githubId))
            .headers { it.setBearerAuth(request.githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(object : ParameterizedTypeReference<List<GitRepoClientResponse>>() {})
            ?: throw RestClientException.memberGitRepo()
}
