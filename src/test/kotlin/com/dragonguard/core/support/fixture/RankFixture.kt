package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.member.Tier
import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.domain.rank.dto.MemberRankResponse
import com.dragonguard.core.domain.rank.dto.OrganizationRankResponse

class RankFixture {

    companion object {
        fun createMemberRankResponse(): List<MemberRankResponse> {
            val responses = listOf(
                MemberRankResponse(
                    id = 1L,
                    githubId = "dragon",
                    tier = Tier.GOLD,
                    profileImage = "http://profileImageUrl",
                ),
                MemberRankResponse(
                    id = 2L,
                    githubId = "dragon",
                    tier = Tier.GOLD,
                    profileImage = "http://profileImageUrl",
                ),
            )

            responses.forEach { it.contributionAmount = 1000L }
            return responses
        }

        fun createOrganizationRankResponse(): List<OrganizationRankResponse> {
            val responses = listOf(
                OrganizationRankResponse(
                    id = 1L,
                    name = "dragon",
                    type = OrganizationType.UNIVERSITY
                ),
                OrganizationRankResponse(
                    id = 2L,
                    name = "dragon",
                    type = OrganizationType.UNIVERSITY
                ),
            )

            responses.forEach { it.contributionAmount = 3000L }
            return responses
        }
    }
}