package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailCheckRequest
import com.dragonguard.core.domain.email.dto.EmailCheckResponse
import com.dragonguard.core.domain.email.dto.EmailSendRequest
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.OrganizationRepository
import com.dragonguard.core.domain.rank.RankService
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailService(
    private val emailRepository: EmailRepository,
    private val emailSender: EmailSender,
    private val organizationRepository: OrganizationRepository,
    private val rankService: RankService,
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

    @Transactional
    fun check(request: EmailCheckRequest, id: Long, member: Member): EmailCheckResponse {
        val email: Email = emailRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.email()
        val isValidCode = email.code == request.code
        if (isValidCode) {
            val organization = organizationRepository.findByIdOrNull(email.organizationId)
                ?: throw EntityNotFoundException.organization()
            organization.addMember(member)
            rankService.addOrganizationContribution(member, organization)
        }
        return EmailCheckResponse(isValidCode)
    }
}
