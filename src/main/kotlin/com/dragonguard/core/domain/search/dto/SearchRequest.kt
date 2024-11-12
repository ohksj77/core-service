package com.dragonguard.core.domain.search.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class SearchRequest(
    @field:NotBlank
    var q: String? = null,
    @field:Positive
    var page: Int? = null,
)
