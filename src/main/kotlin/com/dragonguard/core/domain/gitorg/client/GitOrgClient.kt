package com.dragonguard.core.domain.gitorg.client

import com.dragonguard.core.domain.gitorg.client.dto.GitOrgClientRequest
import com.dragonguard.core.domain.gitorg.client.dto.GitOrgClientResponse
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GitOrgClient(
    private val openApiRestClient: RestClient,
) {
    companion object {
        private const val PATH = "users/%s/orgs?per_page=100"
    }

    fun request(request: GitOrgClientRequest): List<GitOrgClientResponse> =
        openApiRestClient
            .get()
            .uri(PATH.format(request.githubId))
            .headers { it.setBearerAuth(request.githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(object : ParameterizedTypeReference<List<GitOrgClientResponse>>() {})
            ?: throw RestClientException.codeReview()
}
