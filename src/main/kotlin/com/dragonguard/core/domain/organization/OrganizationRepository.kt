package com.dragonguard.core.domain.organization

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrganizationRepository : JpaRepository<Organization, Long> {

    @Query("SELECT o FROM Organization o WHERE o.organizationStatus = :organizationStatus")
    fun findAllByOrganizationStatus(organizationStatus: OrganizationStatus, pageable: Pageable): List<Organization>
}
