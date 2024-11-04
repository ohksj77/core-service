package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.client.CodeReviewClient
import com.dragonguard.core.domain.contribution.client.CommitClient
import com.dragonguard.core.domain.contribution.client.IssueClient
import com.dragonguard.core.domain.contribution.client.PullRequestClient
import com.dragonguard.core.domain.contribution.client.dto.ContributionClientRequest
import com.dragonguard.core.domain.contribution.client.dto.ContributionClientResponse
import com.dragonguard.core.domain.contribution.dto.ContributionClientResult
import com.dragonguard.core.domain.member.Member
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@Service
class ContributionClientService(
    private val codeReviewClient: CodeReviewClient,
    private val commitClient: CommitClient,
    private val issueClient: IssueClient,
    private val pullRequestClient: PullRequestClient,
    @Qualifier("virtualThreadExecutor") private val executorService: ExecutorService,
) {
    companion object {
        private const val DEFAULT_CONTRIBUTION = 0
    }

    fun getContributions(
        member: Member,
        year: Int,
    ): ContributionClientResult =
        executorService.use {
            val request = ContributionClientRequest(member.githubId, member.githubToken!!, year)

            val pullRequest = it.submit { getPullRequestContribution(request) }
            val issue = it.submit { getIssueContribution(request) }
            val codeReview = it.submit { getCodeReviewContribution(request) }
            val commit = it.submit { getCommitContribution(request) }

            ContributionClientResult(
                commit.getOrElse(),
                pullRequest.getOrElse(),
                issue.getOrElse(),
                codeReview.getOrElse(),
            )
        }

    private fun Future<*>.getOrElse(): Int =
        try {
            this.get() as Int
        } catch (e: Exception) {
            DEFAULT_CONTRIBUTION
        }

    private fun getPullRequestContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            pullRequestClient.request(request)
        } catch (e: Exception) {
            ContributionClientResponse(0)
        }

    private fun getIssueContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            issueClient.request(request)
        } catch (e: Exception) {
            ContributionClientResponse(0)
        }

    private fun getCodeReviewContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            codeReviewClient.request(request)
        } catch (e: Exception) {
            ContributionClientResponse(0)
        }

    private fun getCommitContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            commitClient.request(request)
        } catch (e: Exception) {
            ContributionClientResponse(0)
        }
}
