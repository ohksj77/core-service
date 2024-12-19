package com.dragonguard.core.domain.gitorg.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitOrgClientResponse(
    val login: String?,
    @JsonProperty("avatar_url")
    val avatarUrl: String?,
)
