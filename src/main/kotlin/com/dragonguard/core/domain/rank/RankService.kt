package com.dragonguard.core.domain.rank

import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.domain.rank.dto.MemberRankResponse
import com.dragonguard.core.domain.rank.dto.OrganizationRankResponse
import com.dragonguard.core.domain.rank.dto.ProfileRank

interface RankService {
    fun addContribution(
        contributionRequest: ContributionRequest,
        totalAmount: Int,
        member: Member,
    )

    fun getMemberRank(
        page: Long,
        size: Long,
    ): List<MemberRankResponse>

    fun getOrganizationRank(
        organizationType: OrganizationType,
        page: Long,
        size: Long,
    ): List<OrganizationRankResponse>

    fun getAllOrganizationRank(
        page: Long,
        size: Long,
    ): List<OrganizationRankResponse>

    fun getOrganizationMemberRank(
        organizationId: Long,
        page: Long,
        size: Long,
    ): List<MemberRankResponse>

    fun getMemberProfileRank(member: Member): ProfileRank

    fun getMemberRankValue(member: Member): Int
}
