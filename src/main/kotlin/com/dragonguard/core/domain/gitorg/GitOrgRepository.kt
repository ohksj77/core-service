package com.dragonguard.core.domain.gitorg

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GitOrgRepository : JpaRepository<GitOrg, Long> {

    @Query("SELECT gom.gitOrg FROM GitOrgMember gom WHERE gom.memberId = :memberId")
    fun findByMemberId(memberId: Long): List<GitOrg>

    @Query("SELECT go FROM GitOrg go WHERE go.name = :name")
    fun findByName(): GitOrg?
}
