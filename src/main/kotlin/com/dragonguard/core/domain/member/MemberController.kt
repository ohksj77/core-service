package com.dragonguard.core.domain.member

import com.dragonguard.core.global.auth.AuthorizedMember
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("members")
class MemberController(
    private val memberService: MemberService,
) {
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("contributions")
    fun updateContributions(
        @AuthorizedMember member: Member,
    ) = memberService.updateContributions(member)
}
