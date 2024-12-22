package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoInternalRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class GitRepoMemberRedisProducer(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : GitRepoMemberProducer {

    override fun produce(request: GitRepoInternalRequest) {
        val message = objectMapper.writeValueAsString(request)
        redisTemplate.convertAndSend(TOPIC, message)
    }

    companion object {
        private const val TOPIC = "openapi:git-repo-member"
    }
}
