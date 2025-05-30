package com.dragonguard.core.domain.gitorg

import com.dragonguard.core.domain.gitorg.client.GitOrgClient
import com.dragonguard.core.domain.gitorg.client.dto.GitOrgClientRequest
import com.dragonguard.core.domain.gitorg.dto.GitOrgResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberRepository
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GitOrgService(
    private val gitOrgRepository: GitOrgRepository,
    private val gitOrgMapper: GitOrgMapper,
    private val gitOrgClient: GitOrgClient,
    private val memberRepository: MemberRepository,
) {

    fun getOrgsByMemberId(memberId: Long): List<GitOrgResponse> =
        gitOrgMapper.toResponses(gitOrgRepository.findByMemberId(memberId))

    @Transactional
    @Async("virtualAsyncTaskExecutor")
    fun updateGitOrg(memberId: Long, githubToken: String, githubId: String) {
        val response = gitOrgClient.request(GitOrgClientRequest(githubId, githubToken))
        val gitRepos = response.filter {
            it.login != null && it.avatarUrl != null
        }.map {
            val gitRepo =
                gitOrgRepository.findByName(it.login!!) ?: gitOrgRepository.save(GitOrg(it.login, it.avatarUrl!!))
            gitRepo.addMember(getMember(memberId))
            gitRepo
        }.toList()
        gitOrgRepository.saveAll(gitRepos)
    }

    private fun getMember(memberId: Long): Member =
        memberRepository.findByIdOrNull(memberId) ?: throw EntityNotFoundException.member()

    fun getEntity(name: String): GitOrg = gitOrgRepository.findByName(name) ?: throw EntityNotFoundException.gitOrg()
}
