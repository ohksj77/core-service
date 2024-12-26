package com.dragonguard.core.domain.contribution.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ContributionClientResponse(
    @JsonProperty("total_count")
    val totalCount: Int?,
)
