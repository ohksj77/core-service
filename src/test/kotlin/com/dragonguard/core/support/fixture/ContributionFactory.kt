package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.contribution.Contribution
import com.dragonguard.core.domain.contribution.ContributionType
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.Member
import java.time.LocalDateTime

class ContributionFactory {
    companion object {
        fun createEntity(member: Member): Contribution = Contribution(ContributionType.ISSUE, 100, LocalDateTime.now().year, member)

        fun createEntities(member: Member): List<Contribution> =
            listOf(
                Contribution(ContributionType.ISSUE, 100, LocalDateTime.now().year, member),
                Contribution(ContributionType.PULL_REQUEST, 200, LocalDateTime.now().year, member),
                Contribution(ContributionType.COMMIT, 400, LocalDateTime.now().year, member),
                Contribution(ContributionType.CODE_REVIEW, 150, LocalDateTime.now().year, member),
            )

        fun createResponse() =
            listOf(
                ContributionResponse(ContributionType.ISSUE, 100, LocalDateTime.now()),
                ContributionResponse(ContributionType.COMMIT, 300, LocalDateTime.now()),
                ContributionResponse(ContributionType.CODE_REVIEW, 200, LocalDateTime.now()),
                ContributionResponse(ContributionType.PULL_REQUEST, 150, LocalDateTime.now()),
            )
    }
}
