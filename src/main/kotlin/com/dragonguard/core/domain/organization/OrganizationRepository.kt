package com.dragonguard.core.domain.organization

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrganizationRepository : JpaRepository<Organization, Long> {

    @Query("SELECT o FROM Organization o WHERE o.organizationStatus = :organizationStatus")
    fun findAllByOrganizationStatus(organizationStatus: OrganizationStatus, pageable: Pageable): List<Organization>

    @Query("SELECT o.id FROM Organization o WHERE o.name = :name")
    fun findIdByNameOrNull(name: String): Long?

    @EntityGraph(
        attributePaths = ["members", "members.contributions.contributions"],
    )
    @Query("SELECT o FROM Organization o WHERE o.organizationType = :type")
    fun findAllByOrganizationType(type: OrganizationType, pageable: Pageable): List<Organization>

    @EntityGraph(
        attributePaths = ["members", "members.contributions.contributions"],
    )
    fun findAllByOrganizationTypeAndNameContainingIgnoreCase(
        organizationType: OrganizationType,
        name: String,
        pageable: Pageable
    ): List<Organization>
}
