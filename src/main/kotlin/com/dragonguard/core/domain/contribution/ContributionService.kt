package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.member.Member
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContributionService(
    private val contributionRepository: ContributionRepository,
    private val contributionClientService: ContributionClientService,
    private val contributionMapper: ContributionMapper,
) {
    fun updateContributions(member: Member) {
        val year: Int = LocalDateTime.now().year
        val contributionClientResult = contributionClientService.getContributions(member, year)

        contributionRepository.saveAll(
            contributionMapper.toEntities(contributionClientResult, member, year),
        )
    }
}
