package com.dragonguard.core.domain.gitrepo.client

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientRequest
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GitRepoLanguageClient(
    private val openApiRestClient: RestClient,
) {
    companion object {
        private const val PATH = "repos/%s/languages"
    }

    fun request(request: GitRepoDetailsClientRequest): Map<String, Int> =
        openApiRestClient
            .get()
            .uri(PATH.format(request.name))
            .headers { it.setBearerAuth(request.githubToken) }
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(object : ParameterizedTypeReference<Map<String, Int>>() {})
            ?: throw RestClientException.gitRepoLanguage()
}
