package com.dragonguard.core.domain.search.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class SearchMemberRequest(
    @field:NotBlank
    val name: String,
    @field:Positive
    val page: Int,
)