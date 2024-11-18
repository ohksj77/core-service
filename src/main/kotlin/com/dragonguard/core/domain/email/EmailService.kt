package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailSendRequest
import com.dragonguard.core.domain.member.Member
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailRepository: EmailRepository,
    private val emailSender: EmailSender,
) {
    fun send(organizationId: Long, member: Member): Long? {
        val email: Email = emailRepository.save(Email(organizationId, member.id!!))
        emailSender.send(EmailSendRequest(member.email!!, email.code))
        return email.id
    }

    fun deleteCode(id: Long) {
        emailRepository.deleteById(id)
    }
}
