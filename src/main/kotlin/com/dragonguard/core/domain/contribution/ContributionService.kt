package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.Member
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContributionService(
    private val contributionRepository: ContributionRepository,
    private val contributionClientService: ContributionClientService,
    private val contributionMapper: ContributionMapper,
) {
    @Async
    fun updateContributions(member: Member) {
        val year: Int = LocalDateTime.now().year
        val contributionClientResult = contributionClientService.getContributions(member, year)

        contributionRepository.saveAll(
            contributionMapper.toEntities(contributionClientResult, member, year),
        )
    }

    fun getMemberContributions(memberId: Long): List<ContributionResponse> =
        contributionMapper.toResponses(
            contributionRepository
                .findByMemberId(memberId),
        )
}
