package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.email.EmailService
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.dto.OrganizationJoinRequest
import com.dragonguard.core.domain.organization.dto.OrganizationRequest
import com.dragonguard.core.domain.organization.dto.OrganizationResponse
import com.dragonguard.core.global.dto.IdResponse
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper,
    private val emailService: EmailService,
) {
    fun create(organizationRequest: OrganizationRequest): IdResponse {
        val organization = Organization(
            organizationRequest.name!!,
            organizationRequest.emailEndPoint!!,
            organizationRequest.organizationType!!
        )
        val id = organizationRepository.save(organization).id
        return IdResponse(id)
    }

    @Transactional
    fun join(member: Member, request: OrganizationJoinRequest): IdResponse {
        val organization = getEntity(request.organizationId!!)
        organization.validateAndUpdateEmail(member, request.email!!)
        val emailCodeId = emailService.send(organization.id!!, member)
        return IdResponse(emailCodeId)
    }

    private fun getEntity(id: Long): Organization {
        return organizationRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.organization()
    }

    fun getOrganizationId(name: String): IdResponse {
        val id = (organizationRepository.findIdByNameOrNull(name)
            ?: throw EntityNotFoundException.organization())

        return IdResponse(id)
    }

    fun findByType(type: OrganizationType, pageable: Pageable): List<OrganizationResponse> =
        organizationMapper.toResponses(organizationRepository.findAllByOrganizationType(type, pageable))

    fun search(type: OrganizationType, query: String, pageable: Pageable): List<OrganizationResponse> =
        organizationMapper.toResponses(
            organizationRepository.findAllByOrganizationTypeAndNameContainingIgnoreCase(
                type,
                query,
                pageable
            )
        )
}
