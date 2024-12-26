package com.dragonguard.core.domain.gitrepo.dto

import java.util.*

data class SummaryResponse(
    val count: Long?,
    val sum: Long?,
    val min: Int?,
    val max: Int?,
    val average: Double?
) {
    companion object {
        fun empty(): SummaryResponse = SummaryResponse(0, 0, 0, 0, 0.0)

        fun from(statistics: IntSummaryStatistics) =
            SummaryResponse(statistics.count, statistics.sum, statistics.min, statistics.max, statistics.average)
    }
}
