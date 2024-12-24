package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitorg.GitOrgService
import com.dragonguard.core.domain.gitrepo.client.GitOrgGitRepoClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoMemberInternalClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoSparkLineClient
import com.dragonguard.core.domain.gitrepo.client.MemberGitRepoClient
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoInternalRequest
import com.dragonguard.core.domain.gitrepo.dto.GitOrgGitRepoResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoResponse
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
    private val gitRepoMapper: GitRepoMapper,
    private val gitOrgGitRepoClient: GitOrgGitRepoClient,
    private val memberGitRepoClient: MemberGitRepoClient,
    private val gitRepoMemberProducer: GitRepoMemberProducer,
    private val gitRepoSparkLineClient: GitRepoSparkLineClient,
    private val gitRepoMemberInternalClient: GitRepoMemberInternalClient,
    private val gitRepoMemberRepository: GitRepoMemberRepository,
    private val memberRepository: MemberRepository,
    private val gitOrgService: GitOrgService,
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

    fun getGitOrgGitRepos(name: String, member: Member): GitOrgGitRepoResponse {
        val gitOrg = gitOrgService.getEntity(name)
        val response = gitOrgGitRepoClient.request(GitRepoClientRequest(gitOrg.name, member.githubToken!!))
        return GitOrgGitRepoResponse(gitOrg.profileImage, response.map { it.fullName!! }.toSet())
    }

    fun getGitRepoDetails(name: String, memberId: Long): GitRepoResponse {
        val member = getMember(memberId)
        val sparkLineResponse = gitRepoSparkLineClient.request(GitRepoDetailsClientRequest(name, member.githubToken!!))
        val gitRepo = getEntity(name)

        if (gitRepo.hasMemberContribution()) {
            gitRepoMemberProducer.produce(
                GitRepoInternalRequest(
                    gitRepo.id!!,
                    memberId,
                    member.githubToken!!,
                    name
                )
            )
            return gitRepoMapper.toGitRepoResponse(sparkLineResponse, gitRepo.gitRepoMembers)
        }

        val gitRepoMemberResponse = gitRepoMemberInternalClient.request(
            GitRepoInternalRequest(
                gitRepo.id!!,
                memberId,
                member.githubToken!!,
                name
            )
        )
        val gitRepoMembers = gitRepoMemberRepository.findAllByIdIn(gitRepoMemberResponse.gitRepoMemberIds)
        return gitRepoMapper.toGitRepoResponse(sparkLineResponse, gitRepoMembers)
    }

    private fun getEntity(name: String): GitRepo =
        gitRepoRepository.findByName(name) ?: throw EntityNotFoundException.gitRepo()

    fun compare(first: String, second: String, memberId: Long): GitRepoCompareResponse {
        val firstGitRepoResponse = getGitRepoDetails(first, memberId)
        val secondGitRepoResponse = getGitRepoDetails(second, memberId)

        return gitRepoMapper.toGitRepoCompareResponse(firstGitRepoResponse, secondGitRepoResponse)
    }
}
