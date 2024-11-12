package com.dragonguard.core.domain.search

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.search.client.dto.SearchGitRepoClientResponse
import com.dragonguard.core.domain.search.client.dto.SearchMemberClientResponse
import com.dragonguard.core.domain.search.dto.SearchRequest
import com.dragonguard.core.global.auth.AuthorizedMember
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("search")
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping(params = ["type=MEMBER"])
    fun getUsersSearchResult(
        @ModelAttribute @Valid request: SearchRequest,
        @AuthorizedMember member: Member,
    ): List<SearchMemberClientResponse.Companion.SearchMemberResponse> = searchService.searchMembers(request, member)

    @GetMapping(params = ["type=GIT_REPO"])
    fun getGitReposSearchResult(
        @ModelAttribute @Valid request: SearchRequest,
        @RequestParam(required = false) filters: List<String>?,
        @AuthorizedMember member: Member,
    ): List<SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem> = searchService.searchGitRepos(request, filters, member)
}
