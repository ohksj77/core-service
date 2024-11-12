package com.dragonguard.core.domain.search.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class SearchGitRepoRequest(
    @field:NotBlank
    val q: String,
    @field:Positive
    val page: Int,
    val filters: List<String>?,
)
