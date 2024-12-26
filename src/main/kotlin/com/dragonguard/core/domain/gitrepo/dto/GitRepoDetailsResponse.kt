package com.dragonguard.core.domain.gitrepo.dto

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientResponse

data class GitRepoDetailsResponse(
    val gitRepo: GitRepoDetailsClientResponse?,
    val statistics: StatisticsResponse?,
    val languages: Map<String, Int>?,
    val languagesStats: SummaryResponse?,
    val profileUrls: List<String>?
)
