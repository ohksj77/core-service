package com.dragonguard.core.domain.admin

import com.dragonguard.core.domain.admin.dto.AdminDecideRequest
import com.dragonguard.core.domain.admin.dto.AdminOrganizationResponse
import com.dragonguard.core.domain.organization.Organization
import com.dragonguard.core.domain.organization.OrganizationRepository
import com.dragonguard.core.domain.organization.OrganizationStatus
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val organizationRepository: OrganizationRepository,
    private val adminMapper: AdminMapper,
) {
    @Transactional
    fun decide(
        id: Long,
        request: AdminDecideRequest
    ): List<AdminOrganizationResponse> {
        val organization: Organization =
            organizationRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.organization()
        val beforeStatus: OrganizationStatus = organization.organizationStatus

        if (request.organizationStatus == OrganizationStatus.DENIED) {
            organizationRepository.delete(organization)
        }
        organization.approve()

        return findOrganizations(beforeStatus, PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE))
    }

    @Transactional(readOnly = true)
    fun findOrganizationsByStatus(
        status: OrganizationStatus,
        pageable: Pageable,
    ): List<AdminOrganizationResponse> =
        findOrganizations(
            status,
            pageable
        )

    private fun findOrganizations(
        status: OrganizationStatus,
        pageable: Pageable,
    ): List<AdminOrganizationResponse> {
        val organizations: List<Organization> =
            organizationRepository.findAllByOrganizationStatus(
                status, pageable
            )
        return adminMapper.toResponses(organizations)
    }

    companion object {
        const val DEFAULT_PAGE: Int = 0
        const val DEFAULT_PAGE_SIZE: Int = 20
    }
}
