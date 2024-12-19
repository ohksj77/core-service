package com.dragonguard.core.domain.gitorg

import com.dragonguard.core.domain.gitorg.dto.GitOrgResponse
import com.dragonguard.core.global.auth.AuthorizedMemberId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("git-orgs")
class GitOrgController(
    private val gitOrgService: GitOrgService,
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("me")
    fun getOrgs(@AuthorizedMemberId memberId: Long): List<GitOrgResponse> =
        gitOrgService.getOrgsByMemberId(memberId)
}
