package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.dto.OrganizationJoinRequest
import com.dragonguard.core.domain.organization.dto.OrganizationRequest
import com.dragonguard.core.domain.organization.dto.OrganizationResponse
import com.dragonguard.core.global.auth.AuthorizedMember
import com.dragonguard.core.global.dto.IdResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("id")
    fun getOrganizationId(@RequestParam name: String): IdResponse = organizationService.getOrganizationId(name)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun findByType(@RequestParam type: OrganizationType, pageable: Pageable): List<OrganizationResponse> =
        organizationService.findByType(type, pageable)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("search")
    fun search(
        @RequestParam type: OrganizationType,
        @RequestParam q: String,
        pageable: Pageable
    ): List<OrganizationResponse> =
        organizationService.search(type, q, pageable)
}
