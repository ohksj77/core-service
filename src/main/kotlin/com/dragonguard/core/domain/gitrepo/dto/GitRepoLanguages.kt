package com.dragonguard.core.domain.gitrepo.dto

import java.util.*

class GitRepoLanguages(
    val languages: Map<String, Int>
) {

    val statistics: IntSummaryStatistics
        get() {
            if (languages.keys.isEmpty()) {
                return IntSummaryStatistics(
                    NO_CONTRIBUTION.toLong(),
                    NO_CONTRIBUTION,
                    NO_CONTRIBUTION,
                    NO_CONTRIBUTION.toLong()
                )
            }
            return languages.keys.stream().mapToInt { key: String -> languages[key]!! }.summaryStatistics()
        }

    companion object {
        private const val NO_CONTRIBUTION = 0
    }
}
