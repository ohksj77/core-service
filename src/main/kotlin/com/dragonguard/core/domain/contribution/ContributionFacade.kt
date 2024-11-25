package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.rank.RankService
import com.dragonguard.core.domain.rank.dto.ProfileRank
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContributionFacade(
    private val contributionService: ContributionService,
    private val contributionClientService: ContributionClientService,
    private val rankService: RankService,
) {
    @Async("virtualAsyncTaskExecutor")
    fun updateContributions(contributionRequest: ContributionRequest) {
        val year: Int = LocalDateTime.now().year
        val contributionClientResult = contributionClientService.getContributions(contributionRequest, year)

        rankService.addContribution(
            contributionRequest,
            contributionClientResult.getTotal(),
        )
        contributionService.saveContribution(contributionClientResult, contributionRequest.memberId, year)
    }

    fun getMemberContributions(memberId: Long): List<ContributionResponse> =
        contributionService.getMemberContributions(memberId)

    fun getMemberProfileRank(member: Member): ProfileRank = rankService.getMemberProfileRank(member)

    fun getMemberRank(member: Member): Int = rankService.getMemberRank(member)
}
