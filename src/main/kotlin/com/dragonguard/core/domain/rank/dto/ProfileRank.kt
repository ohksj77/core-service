package com.dragonguard.core.domain.rank.dto

data class ProfileRank(
    var memberGithubIds: List<String>,
    var rank: Int,
    var organizationRank: Int,
    var isLast: Boolean,
) {
    private constructor() : this(mutableListOf(), EMPTY_RANK, EMPTY_RANK, true)

    companion object {
        private const val EMPTY_RANK = -1

        fun empty(): ProfileRank = ProfileRank()
    }
}
