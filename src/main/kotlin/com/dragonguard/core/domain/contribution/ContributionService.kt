package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.dto.ContributionClientResult
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.Member
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ContributionService(
    private val contributionMapper: ContributionMapper,
    private val contributionRepository: ContributionRepository,
) {
    @Transactional
    fun saveContribution(
        contributionClientResult: ContributionClientResult,
        member: Member,
        year: Int,
    ) {
        val contributions =
            contributionMapper.toEntities(contributionClientResult, member, year)

        member.addContribution(contributions)
    }

    fun getMemberContributions(memberId: Long): List<ContributionResponse> =
        contributionMapper.toResponses(
            contributionRepository
                .findByMemberId(memberId),
        )
}
