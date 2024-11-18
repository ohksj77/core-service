package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailCheckRequest
import com.dragonguard.core.domain.email.dto.EmailCheckResponse
import com.dragonguard.core.domain.email.dto.EmailSendRequest
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailRepository: EmailRepository,
    private val emailSender: EmailSender,
) {
    fun send(organizationId: Long, member: Member): Long? {
        val email: Email = emailRepository.save(Email(organizationId, member.email!!))
        emailSender.send(EmailSendRequest(member.email!!, email.code))
        return email.id
    }

    fun deleteCode(id: Long) {
        emailRepository.deleteById(id)
    }

    fun resend(id: Long) {
        val email: Email = emailRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.email()
        emailSender.send(EmailSendRequest(email.email, email.code))
    }

    fun check(request: EmailCheckRequest, id: Long): EmailCheckResponse {
        val email: Email = emailRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.email()
        return EmailCheckResponse(email.code == request.code)
    }
}
