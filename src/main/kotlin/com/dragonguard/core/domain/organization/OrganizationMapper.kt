package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.organization.dto.OrganizationResponse
import org.springframework.stereotype.Component

@Component
class OrganizationMapper {

    fun toResponses(organizations: List<Organization>): List<OrganizationResponse> {
        return organizations.map { toResponse(it) }
    }

    private fun toResponse(organization: Organization) = OrganizationResponse(
        organization.id,
        organization.name,
        organization.organizationType,
        organization.emailEndpoint,
        organization.memberContributionAmount(),
    )
}
