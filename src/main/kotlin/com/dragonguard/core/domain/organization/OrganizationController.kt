package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.dto.OrganizationJoinRequest
import com.dragonguard.core.domain.organization.dto.OrganizationRequest
import com.dragonguard.core.global.auth.AuthorizedMember
import com.dragonguard.core.global.dto.IdResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("organizations")
class OrganizationController(
    private val organizationService: OrganizationService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createOrganization(@RequestBody @Valid organizationRequest: OrganizationRequest): IdResponse =
        organizationService.create(organizationRequest)

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("join")
    fun joinOrganization(
        @AuthorizedMember member: Member,
        @RequestBody @Valid organizationJoinRequest: OrganizationJoinRequest
    ): IdResponse =
        organizationService.join(member, organizationJoinRequest)
}
