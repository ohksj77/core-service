package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitrepo.client.MemberGitRepoClient
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientRequest
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberRepository
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GitRepoService(
    private val gitRepoRepository: GitRepoRepository,
    private val memberGitRepoClient: MemberGitRepoClient,
    private val memberRepository: MemberRepository
) {
    fun getNamesByMemberId(memberId: Long): List<String> = gitRepoRepository.findNamesByMemberId(memberId)

    @Transactional
    @Async("virtualAsyncTaskExecutor")
    fun updateGitRepo(memberId: Long, githubToken: String, githubId: String) {
        val response = memberGitRepoClient.request(GitRepoClientRequest(githubId, githubToken))
        val gitRepos = response.filter { it.fullName != null }.map {
            val gitRepo = gitRepoRepository.findByName(it.fullName!!) ?: gitRepoRepository.save(GitRepo(it.fullName))
            gitRepo.addMember(getMember(memberId))
            gitRepo
        }.toList()
        gitRepoRepository.saveAll(gitRepos)
    }

    fun getMember(memberId: Long): Member =
        memberRepository.findByIdOrNull(memberId) ?: throw EntityNotFoundException.member()
}
