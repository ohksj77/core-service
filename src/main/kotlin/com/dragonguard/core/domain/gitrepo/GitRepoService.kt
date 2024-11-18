package com.dragonguard.core.domain.gitrepo

import org.springframework.stereotype.Service

@Service
class GitRepoService(
    private val gitRepoRepository: GitRepoRepository,
) {
    fun getNamesByMemberId(memberId: Long): List<String> = gitRepoRepository.findNamesByMemberId(memberId)
}
