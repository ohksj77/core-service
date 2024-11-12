package com.dragonguard.core.domain.contribution

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ContributionRepository : JpaRepository<Contribution, Long> {
    @Query("SELECT c FROM Contribution c WHERE c.member.id = :memberId")
    fun findByMemberId(memberId: Long): List<Contribution>
}
