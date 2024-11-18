package com.dragonguard.core.domain.admin

import com.dragonguard.core.domain.admin.dto.AdminOrganizationResponse
import com.dragonguard.core.domain.organization.Organization
import org.springframework.stereotype.Component

@Component
class AdminMapper {

    fun toResponses(organizations: List<Organization>): List<AdminOrganizationResponse> {
        return organizations.map { toResponse(it) }
    }

    private fun toResponse(organization: Organization) =
        AdminOrganizationResponse(
            organization.id!!,
            organization.name,
            organization.organizationType,
            organization.emailEndpoint,
        )
}
