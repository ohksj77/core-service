package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.contribution.Contribution
import com.dragonguard.core.domain.contribution.ContributionType
import java.time.LocalDateTime

class ContributionFactory {
    companion object {
        fun createEntity(memberId: Long): Contribution = Contribution(ContributionType.ISSUE, 100, LocalDateTime.now().year, memberId)

        fun createEntities(memberId: Long): List<Contribution> =
            listOf(
                Contribution(ContributionType.ISSUE, 100, LocalDateTime.now().year, memberId),
                Contribution(ContributionType.PULL_REQUEST, 200, LocalDateTime.now().year, memberId),
                Contribution(ContributionType.COMMIT, 400, LocalDateTime.now().year, memberId),
                Contribution(ContributionType.CODE_REVIEW, 150, LocalDateTime.now().year, memberId),
            )
    }
}
