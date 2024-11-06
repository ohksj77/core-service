package com.dragonguard.core.domain.contribution.dto

import com.dragonguard.core.domain.contribution.ContributionType
import java.time.LocalDateTime

data class ContributionResponse(
    val contributeType: ContributionType,
    val amount: Int,
    val createdAt: LocalDateTime,
)
