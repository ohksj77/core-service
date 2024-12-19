package com.dragonguard.core.domain.rank.exception

class RankAccessException(
    override val message: String,
) : IllegalStateException(message) {
    companion object {
        fun update(e: Exception): RankAccessException = RankAccessException("Cannot update rank: ${e.message}")

        fun get(e: Exception): RankAccessException = RankAccessException("Cannot get rank: ${e.message}")

        fun parseJson(e: Exception): RankAccessException = RankAccessException("Cannot parse json: ${e.message}")
    }
}
