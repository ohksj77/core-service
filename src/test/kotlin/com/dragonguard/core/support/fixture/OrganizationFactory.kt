package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.admin.dto.AdminOrganizationResponse
import com.dragonguard.core.domain.organization.OrganizationType

class OrganizationFactory {
    companion object {
        fun createOrganizationResponses(): List<AdminOrganizationResponse> =
            listOf(
                AdminOrganizationResponse(
                    1L,
                    "test-org",
                    OrganizationType.UNIVERSITY,
                    "tukorea.ac.kr",
                ),
                AdminOrganizationResponse(
                    2L,
                    "test-org2",
                    OrganizationType.UNIVERSITY,
                    "tukorea.ac.kr",
                ),
            )
    }
}