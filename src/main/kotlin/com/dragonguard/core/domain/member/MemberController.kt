package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.dto.MemberDetailsResponse
import com.dragonguard.core.domain.member.dto.MemberProfileResponse
import com.dragonguard.core.domain.member.dto.MemberVerifyResponse
import com.dragonguard.core.global.auth.AuthorizedMember
import com.dragonguard.core.global.auth.AuthorizedMemberId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("contributions")
    fun getContributions(
        @AuthorizedMemberId memberId: Long,
    ): List<ContributionResponse> = memberService.getContributions(memberId)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("me")
    fun getProfile(
        @AuthorizedMember member: Member,
    ): MemberProfileResponse = memberService.getProfile(member)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("verify")
    fun verify(
        @AuthorizedMember member: Member,
    ): MemberVerifyResponse = memberService.verify(member)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun delete(
        @AuthorizedMemberId memberId: Long,
    ) = memberService.delete(memberId)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("details/me")
    fun getDetails(
        @AuthorizedMemberId memberId: Long,
    ): MemberDetailsResponse = memberService.getDetailsById(memberId)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("details")
    fun getDetails(
        @RequestParam githubId: String,
    ): MemberDetailsResponse = memberService.getDetailsByGithubId(githubId)
}
