package com.dragonguard.core.domain.gitrepo.client

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoInternalClientResponse
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoInternalRequest
import com.dragonguard.core.global.exception.RestClientException
import org.springframework.http.MediaType
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GitRepoMemberInternalClient(
    private val internalRestClient: RestClient,
) {
    companion object {
        private const val PATH = "git-repos"
    }

    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
    )
    fun request(request: GitRepoInternalRequest): GitRepoInternalClientResponse =
        internalRestClient
            .post()
            .uri(PATH)
            .body(request)
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(Charsets.UTF_8)
            .retrieve()
            .body(GitRepoInternalClientResponse::class.java)
            ?: throw RestClientException.gitRepoInternal()
}
