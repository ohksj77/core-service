package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.dto.ContributionClientResult
import com.dragonguard.core.domain.member.Member
import org.springframework.stereotype.Component

@Component
class ContributionMapper {
    fun toEntities(
        contributionClientResult: ContributionClientResult,
        member: Member,
        year: Int,
    ) = ContributionType.entries.map { type ->
        Contribution(
            type,
            when (type) {
                ContributionType.COMMIT -> contributionClientResult.commit
                ContributionType.PULL_REQUEST -> contributionClientResult.pullRequest
                ContributionType.ISSUE -> contributionClientResult.issue
                ContributionType.CODE_REVIEW -> contributionClientResult.codeReview
            },
            year,
            member.id!!,
        )
    }
}
