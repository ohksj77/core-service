package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.dto.ContributionClientResult
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.MemberRepository
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContributionService(
    private val contributionMapper: ContributionMapper,
    private val contributionRepository: ContributionRepository,
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun saveContribution(
        contributionClientResult: ContributionClientResult,
        memberId: Long,
        year: Int,
    ) {
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw EntityNotFoundException.member()
        val contributions =
            contributionMapper.toEntities(contributionClientResult, member, year)

        memberRepository.findByIdOrNull(member.id)?.addContribution(contributions)
    }

    fun getMemberContributions(memberId: Long): List<ContributionResponse> =
        contributionMapper.toResponses(
            contributionRepository
                .findByMemberId(memberId),
        )
}
