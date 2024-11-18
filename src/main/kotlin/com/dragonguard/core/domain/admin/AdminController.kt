package com.dragonguard.core.domain.admin

import com.dragonguard.core.domain.admin.dto.AdminDecideRequest
import com.dragonguard.core.domain.admin.dto.AdminOrganizationResponse
import com.dragonguard.core.domain.organization.OrganizationStatus
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
class AdminController(
    private val adminService: AdminService,
) {
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("check")
    fun check() {
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("organizations/{id}/decide")
    fun decide(
        @PathVariable id: Long,
        @RequestBody @Valid request: AdminDecideRequest
    ): List<AdminOrganizationResponse> =
        adminService.decide(id, request)

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("organizations")
    fun findOrganizationsByStatus(
        @RequestParam status: OrganizationStatus,
        @PageableDefault(page = 0, size = 20) pageable: Pageable
    ): List<AdminOrganizationResponse> =
        adminService.findOrganizationsByStatus(status, pageable)
}
