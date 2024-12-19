package com.dragonguard.core.domain.gitrepo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GitRepoRepository : JpaRepository<GitRepo, Long> {

    @Query("SELECT DISTINCT gr.name FROM GitRepo gr JOIN gr.gitRepoMembers grm WHERE grm.member.id = :memberId")
    fun findNamesByMemberId(memberId: Long): List<String>

    @Query("SELECT gr FROM GitRepo gr WHERE gr.name = :name")
    fun findByName(): GitRepo?
}
