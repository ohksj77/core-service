package com.dragonguard.core.domain.gitrepo.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GitRepoDetailsClientResponse(
    @JsonProperty("full_name")
    val fullName: String?,
    @JsonProperty("forks_count")
    val forksCount: Int?,
    @JsonProperty("stargazers_count")
    val stargazersCount: Int?,
    @JsonProperty("watchers_count")
    val watchersCount: Int?,
    @JsonProperty("open_issues_count")
    val openIssuesCount: Int?,
    @JsonProperty("closed_issues_count")
    var closedIssuesCount: Int?,
    @JsonProperty("subscribers_count")
    val subscribersCount: Int?
)
