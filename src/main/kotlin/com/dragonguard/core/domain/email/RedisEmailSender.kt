package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailSendRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisEmailSender(
    private val redisTemplate: RedisTemplate<String, String>
) : EmailSender {
    override fun send(request: EmailSendRequest) {
        redisTemplate.convertAndSend(TOPIC, request.toString())
    }

    companion object {
        private const val TOPIC = "alert:email"
    }
}
