package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.admin.dto.AdminOrganizationResponse
import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.domain.organization.dto.OrganizationResponse

class OrganizationFactory {
    companion object {
        fun createAdminOrganizationResponses(): List<AdminOrganizationResponse> =
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

        fun createOrganizationResponses(): List<OrganizationResponse> =
            listOf(
                OrganizationResponse(
                    1L,
                    "한국공학대학교",
                    OrganizationType.UNIVERSITY,
                    "tukorea.ac.kr",
                    1000,
                ),
                OrganizationResponse(
                    2L,
                    "test-org2",
                    OrganizationType.UNIVERSITY,
                    "abc.com",
                    300,
                ),
            )
    }
}