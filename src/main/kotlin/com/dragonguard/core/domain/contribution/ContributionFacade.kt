package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberRepository
import com.dragonguard.core.domain.rank.RankService
import com.dragonguard.core.domain.rank.dto.ProfileRank
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContributionFacade(
    private val contributionService: ContributionService,
    private val contributionClientService: ContributionClientService,
    private val rankService: RankService,
    private val memberRepository: MemberRepository,
) {
    @Async("virtualAsyncTaskExecutor")
    fun updateContributions(contributionRequest: ContributionRequest) {
        val year: Int = LocalDateTime.now().year
        val contributionClientResult = contributionClientService.getContributions(contributionRequest, year)
        val updated =
            contributionService.saveContribution(contributionClientResult, contributionRequest.memberId, year)
        
        if (!updated) {
            return
        }

        rankService.addContribution(
            contributionRequest,
            contributionClientResult.getTotal(),
            getMember(contributionRequest.memberId)
        )
    }

    private fun getMember(memberId: Long): Member =
        memberRepository.findById(memberId).orElseThrow { throw EntityNotFoundException.member() }

    fun getMemberContributions(memberId: Long): List<ContributionResponse> =
        contributionService.getMemberContributions(memberId)

    fun getMemberProfileRank(member: Member): ProfileRank = rankService.getMemberProfileRank(member)

    fun getMemberRank(member: Member): Int = rankService.getMemberRankValue(member)
}
