package com.dragonguard.core.domain.gitrepo.dto

data class StatisticsResponse(
    val commitStats: SummaryResponse?,
    val additionStats: SummaryResponse?,
    val deletionStats: SummaryResponse?
)
