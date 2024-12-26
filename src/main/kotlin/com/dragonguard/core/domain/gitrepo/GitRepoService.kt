package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitorg.GitOrgService
import com.dragonguard.core.domain.gitrepo.client.GitOrgGitRepoClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoDetailsClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoIssueClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoLanguageClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoMemberInternalClient
import com.dragonguard.core.domain.gitrepo.client.GitRepoSparkLineClient
import com.dragonguard.core.domain.gitrepo.client.MemberGitRepoClient
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientRequest
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoDetailsClientResponse
import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoInternalRequest
import com.dragonguard.core.domain.gitrepo.dto.GitOrgGitRepoResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoDetailsResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoLanguages
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoResponse
import com.dragonguard.core.domain.gitrepo.dto.StatisticsResponse
import com.dragonguard.core.domain.gitrepo.dto.SummaryResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberRepository
import com.dragonguard.core.domain.search.client.dto.SearchGitRepoClientResponse
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
    private val gitRepoDetailsClient: GitRepoDetailsClient,
    private val gitRepoLanguageClient: GitRepoLanguageClient,
    private val gitRepoIssueClient: GitRepoIssueClient,
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
        response.filter { it.fullName != null }.forEach {
            val gitRepo = gitRepoRepository.findByName(it.fullName!!) ?: gitRepoRepository.save(GitRepo(it.fullName))
            gitRepo.addMember(getMember(memberId))
        }
    }

    fun getMember(memberId: Long): Member =
        memberRepository.findByIdOrNull(memberId) ?: throw EntityNotFoundException.member()

    fun getGitOrgGitRepos(name: String, member: Member): GitOrgGitRepoResponse {
        val gitOrg = gitOrgService.getEntity(name)
        val response = gitOrgGitRepoClient.request(GitRepoClientRequest(gitOrg.name, member.githubToken!!))
        return GitOrgGitRepoResponse(gitOrg.profileImage, response.map { it.fullName!! }.toSet())
    }

    fun getGitRepoDetails(name: String, memberId: Long): GitRepoResponse {
        val sparkLine =
            gitRepoSparkLineClient.request(GitRepoDetailsClientRequest(name, getMember(memberId).githubToken!!))

        val member = getMember(memberId)
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
            return gitRepoMapper.toGitRepoResponse(sparkLine, gitRepo.gitRepoMembers)
        }

        val gitRepoMemberResponse = gitRepoMemberInternalClient.request(
            GitRepoInternalRequest(
                gitRepo.id!!,
                memberId,
                member.githubToken!!,
                name
            )
        )
        return gitRepoMapper.toGitRepoResponse(
            sparkLine,
            gitRepoMemberRepository.findAllByIdIn(gitRepoMemberResponse.gitRepoMemberIds!!)
        )
    }

    fun requestAndGetGitRepoMembers(name: String, memberId: Long): List<GitRepoMember> {
        val member = getMember(memberId)
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
            return gitRepo.gitRepoMembers
        }

        val gitRepoMemberResponse = gitRepoMemberInternalClient.request(
            GitRepoInternalRequest(
                gitRepo.id!!,
                memberId,
                member.githubToken!!,
                name
            )
        )
        return gitRepoMemberRepository.findAllByIdIn(gitRepoMemberResponse.gitRepoMemberIds!!)
    }

    private fun getEntity(name: String): GitRepo =
        gitRepoRepository.findByName(name) ?: throw EntityNotFoundException.gitRepo()

    fun compareGitReposMembers(first: String, second: String, memberId: Long): GitRepoMemberCompareResponse {
        val firstGitRepoResponse = requestAndGetGitRepoMembers(first, memberId)
        val secondGitRepoResponse = requestAndGetGitRepoMembers(second, memberId)

        return gitRepoMapper.toGitRepoCompareResponse(firstGitRepoResponse, secondGitRepoResponse)
    }

    @Transactional
    fun saveGitRepos(response: List<SearchGitRepoClientResponse.Companion.SearchGitRepoClientResponseItem>) {
        response.forEach { gitRepoRepository.findByName(it.fullName!!) ?: gitRepoRepository.save(GitRepo(it.fullName)) }
    }

    fun compare(first: String, second: String, member: Member): GitRepoCompareResponse {
        val githubToken = member.githubToken!!

        val firstRequest = GitRepoDetailsClientRequest(first, githubToken)
        val secondRequest = GitRepoDetailsClientRequest(second, githubToken)

        return GitRepoCompareResponse(
            createGitRepoDetailsResponse(first, firstRequest),
            createGitRepoDetailsResponse(second, secondRequest)
        )
    }

    private fun createGitRepoDetailsResponse(
        repoName: String,
        request: GitRepoDetailsClientRequest
    ): GitRepoDetailsResponse {
        val repoResponse = gitRepoDetailsClient.request(request)
        val gitRepoLanguages = GitRepoLanguages(gitRepoLanguageClient.request(request))

        return findGitRepoResponse(repoName, repoResponse, gitRepoLanguages, request.githubToken)
    }

    private fun findGitRepoResponse(
        repoName: String,
        repoResponse: GitRepoDetailsClientResponse,
        gitRepoLanguages: GitRepoLanguages,
        githubToken: String
    ): GitRepoDetailsResponse {
        val gitRepo = gitRepoRepository.findByName(repoName) ?: throw EntityNotFoundException.gitRepo()
        val gitRepoMembers = gitRepo.gitRepoMembers

        repoResponse.closedIssuesCount =
            gitRepoIssueClient.request(GitRepoDetailsClientRequest(repoName, githubToken))

        return GitRepoDetailsResponse(
            repoResponse,
            getStatistics(gitRepo),
            gitRepoLanguages.languages,
            SummaryResponse.from(gitRepoLanguages.statistics),
            getProfileUrls(gitRepoMembers)
        )
    }

    private fun getProfileUrls(gitRepoMembers: List<GitRepoMember>): List<String> =
        gitRepoMembers.map { it.member.profileImage }

    private fun isContributionEmpty(gitRepoMembers: List<GitRepoMember>): Boolean =
        gitRepoMembers.any { it.gitRepoContribution?.commits == null }

    private fun getStatistics(gitRepo: GitRepo): StatisticsResponse {
        val gitRepoMembers = gitRepo.gitRepoMembers
        if (isContributionEmpty(gitRepoMembers)) return getStatisticsResponse(emptyList(), emptyList(), emptyList())

        val commits = getContributionList(gitRepoMembers) { it.gitRepoContribution!!.commits }
        val additions = getContributionList(gitRepoMembers) { it.gitRepoContribution!!.additions }
        val deletions = getContributionList(gitRepoMembers) { it.gitRepoContribution!!.deletions }

        return getStatisticsResponse(commits, additions, deletions)
    }

    private fun getContributionList(
        gitRepoMembers: List<GitRepoMember>,
        function: (GitRepoMember) -> Int
    ): List<Int> =
        gitRepoMembers.map(function)

    private fun getStatisticsResponse(
        commits: List<Int>,
        additions: List<Int>,
        deletions: List<Int>
    ): StatisticsResponse {
        return StatisticsResponse(
            getSummaryResponse(commits),
            getSummaryResponse(additions),
            getSummaryResponse(deletions)
        )
    }

    private fun getSummaryResponse(contributions: List<Int>): SummaryResponse =
        if (contributions.isEmpty()) SummaryResponse.empty() else SummaryResponse.from(
            contributions.stream().mapToInt { it: Int -> it }.summaryStatistics()
        )
}
