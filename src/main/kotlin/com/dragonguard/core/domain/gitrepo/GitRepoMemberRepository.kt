package com.dragonguard.core.domain.gitrepo

import org.springframework.data.jpa.repository.JpaRepository

interface GitRepoMemberRepository : JpaRepository<GitRepoMember, Long> {
    fun findAllByIdIn(ids: List<Long>): List<GitRepoMember>
}