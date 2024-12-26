package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitrepo.dto.GitOrgGitRepoResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.auth.AuthorizedMember
import com.dragonguard.core.global.auth.AuthorizedMemberId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("git-repos")
class GitRepoController(
    private val gitRepoService: GitRepoService,
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("me")
    fun getNamesByMemberId(@AuthorizedMemberId memberId: Long): List<String> =
        gitRepoService.getNamesByMemberId(memberId)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("git-organizations")
    fun getMemberGitOrganizationRepo(
        @RequestParam name: String,
        @AuthorizedMember member: Member,
    ): GitOrgGitRepoResponse =
        gitRepoService.getGitOrgGitRepos(name, member)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun getRepoInfo(
        @RequestParam name: String,
        @AuthorizedMemberId memberId: Long,
    ): GitRepoResponse = gitRepoService.getGitRepoDetails(name, memberId)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("compare/git-repos-members")
    fun compareGitReposMembers(
        @RequestParam first: String,
        @RequestParam second: String,
        @AuthorizedMemberId memberId: Long,
    ): GitRepoMemberCompareResponse = gitRepoService.compareGitReposMembers(first, second, memberId)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("compare")
    fun compare(
        @RequestParam first: String,
        @RequestParam second: String,
        @AuthorizedMember member: Member,
    ): GitRepoCompareResponse = gitRepoService.compare(first, second, member)
}
