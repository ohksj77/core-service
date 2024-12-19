package com.dragonguard.core.domain.rank

import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.domain.rank.dto.MemberRankResponse
import com.dragonguard.core.domain.rank.dto.OrganizationRankResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("ranks")
class RankController(
    private val rankService: RankService
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("members")
    fun getMemberRanks(@RequestParam page: Long, @RequestParam size: Long): List<MemberRankResponse> =
        rankService.getMemberRank(page, size)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("organizations/{organizationId}/members")
    fun getOrganizationMemberRanks(
        @PathVariable organizationId: Long,
        @RequestParam page: Long,
        @RequestParam size: Long
    ): List<MemberRankResponse> =
        rankService.getOrganizationMemberRank(organizationId, page, size)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("organizations")
    fun getOrganizationRanksByType(
        @RequestParam type: OrganizationType,
        @RequestParam page: Long,
        @RequestParam size: Long
    ): List<OrganizationRankResponse> = rankService.getOrganizationRank(type, page, size)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("organizations/all")
    fun getAllOrganizationRanks(@RequestParam page: Long, @RequestParam size: Long): List<OrganizationRankResponse> =
        rankService.getAllOrganizationRank(page, size)
}
