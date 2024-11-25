package com.dragonguard.core.domain.organization.dto

import com.dragonguard.core.domain.organization.OrganizationType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class OrganizationRequest(
    @field:Length(min = 2)
    val name: String?,
    @field:NotBlank
    val emailEndPoint: String?,
    @field:NotNull
    val organizationType: OrganizationType?,
)