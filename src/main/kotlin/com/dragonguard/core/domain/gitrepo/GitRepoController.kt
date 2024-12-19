package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.global.auth.AuthorizedMemberId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("git-repos")
class GitRepoController(
    private val gitRepoService: GitRepoService,
) {
    @GetMapping("me")
    fun getNamesByMemberId(@AuthorizedMemberId memberId: Long): List<String> =
        gitRepoService.getNamesByMemberId(memberId)
}
