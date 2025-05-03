package com.dragonguard.core.domain.search

import com.dragonguard.core.domain.gitrepo.GitRepoService
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberService
import com.dragonguard.core.domain.search.client.SearchGitRepoClient
import com.dragonguard.core.domain.search.client.SearchMemberClient
import com.dragonguard.core.domain.search.client.dto.SearchGitRepoClientResponse
import com.dragonguard.core.domain.search.client.dto.SearchMemberClientResponse
import com.dragonguard.core.domain.search.dto.SearchRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SearchService(
    private val searchMemberClient: SearchMemberClient,
    private val searchGitRepoClient: SearchGitRepoClient,
    private val memberService: MemberService,
    private val gitRepoService: GitRepoService,
) {
    @Transactional(readOnly = true)
    fun searchMembers(
        request: SearchRequest,
        member: Member,
    ): List<SearchMemberClientResponse.Companion.SearchMemberResponse> {
        val result = member.githubToken?.let { searchMemberClient.request(request, it) }
        val serviceMembers = memberService.isServiceMember(result?.items ?: emptyList())

        return result?.toSearchMemberResponse(serviceMembers) ?: emptyList()
    }

    fun searchGitRepos(
        request: SearchRequest,
        filters: List<String>?,
        member: Member,
    ): List<SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem> {
        val response =
            member.githubToken?.let { searchGitRepoClient.request(request, filters, it).items } ?: emptyList()
        gitRepoService.saveGitRepos(response)

        return response
    }
}
