package com.dragonguard.core.domain.rank

import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.domain.rank.dto.MemberRank
import com.dragonguard.core.domain.rank.dto.ProfileRank

interface RankService {
    fun addContribution(
        contributionRequest: ContributionRequest,
        totalAmount: Int,
    )

    fun getMemberRank(
        start: Long,
        end: Long,
    ): List<MemberRank>

    fun getOrganizationRank(
        organizationType: OrganizationType,
        start: Long,
        end: Long,
    ): List<MemberRank>

    fun getOrganizationMemberRank(
        organizationId: Long,
        start: Long,
        end: Long,
    ): List<MemberRank>

    fun getMemberProfileRank(member: Member): ProfileRank

    fun getMemberRank(member: Member): Int
}
