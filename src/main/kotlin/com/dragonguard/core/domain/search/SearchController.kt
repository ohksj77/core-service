package com.dragonguard.core.domain.search

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.search.client.dto.SearchGitRepoClientResponse
import com.dragonguard.core.domain.search.client.dto.SearchMemberClientResponse
import com.dragonguard.core.domain.search.dto.SearchGitRepoRequest
import com.dragonguard.core.domain.search.dto.SearchMemberRequest
import com.dragonguard.core.global.auth.AuthorizedMember
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("search")
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping(params = ["type=MEMBER"])
    fun getUsersSearchResult(
        @RequestAttribute @Valid searchMemberRequest: SearchMemberRequest,
        @AuthorizedMember member: Member,
    ): List<SearchMemberClientResponse.Companion.SearchMemberResponse> = searchService.searchMembers(searchMemberRequest, member)

    @GetMapping(params = ["type=GIT_REPO"])
    fun getGitReposSearchResult(
        @RequestAttribute @Valid searchGitRepoRequest: SearchGitRepoRequest,
        @AuthorizedMember member: Member,
    ): List<SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem> =
        searchService.searchGitRepos(searchGitRepoRequest, member)
}
