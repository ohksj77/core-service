package com.dragonguard.core.domain.gitrepo.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitRepoClientResponse(
    @JsonProperty("full_name")
    val fullName: String?,
)
