package com.dragonguard.core.domain.contribution

import com.dragonguard.core.domain.contribution.client.CodeReviewClient
import com.dragonguard.core.domain.contribution.client.CommitClient
import com.dragonguard.core.domain.contribution.client.IssueClient
import com.dragonguard.core.domain.contribution.client.PullRequestClient
import com.dragonguard.core.domain.contribution.client.dto.ContributionClientRequest
import com.dragonguard.core.domain.contribution.client.dto.ContributionClientResponse
import com.dragonguard.core.domain.contribution.dto.ContributionClientResult
import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Service
class ContributionClientService(
    private val codeReviewClient: CodeReviewClient,
    private val commitClient: CommitClient,
    private val issueClient: IssueClient,
    private val pullRequestClient: PullRequestClient,
) {
    private val logger = LoggerFactory.getLogger(ContributionClientService::class.java)
    private val executor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()

    fun getContributions(contributionRequest: ContributionRequest, year: Int): ContributionClientResult {
        val request = ContributionClientRequest(contributionRequest.githubId, contributionRequest.githubToken, year)

        val pullRequestFuture =
            CompletableFuture.supplyAsync({ getPullRequestContribution(request).totalCount }, executor)
        val issueFuture = CompletableFuture.supplyAsync({ getIssueContribution(request).totalCount }, executor)
        val codeReviewFuture =
            CompletableFuture.supplyAsync({ getCodeReviewContribution(request).totalCount }, executor)
        val commitFuture = CompletableFuture.supplyAsync({ getCommitContribution(request).totalCount }, executor)

        val allOf = CompletableFuture.allOf(pullRequestFuture, issueFuture, codeReviewFuture, commitFuture)
        allOf.join()

        return ContributionClientResult(
            commitFuture.get()!!,
            pullRequestFuture.get()!!,
            issueFuture.get()!!,
            codeReviewFuture.get()!!,
        )
    }

    private fun getPullRequestContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            pullRequestClient.request(request)
        } catch (e: Exception) {
            logger.error("Failed to get pull request contribution", e)
            throw e
        }

    private fun getIssueContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            issueClient.request(request)
        } catch (e: Exception) {
            logger.error("Failed to get issue contribution", e)
            throw e
        }

    private fun getCodeReviewContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            codeReviewClient.request(request)
        } catch (e: Exception) {
            logger.error("Failed to get code review contribution", e)
            throw e
        }

    private fun getCommitContribution(request: ContributionClientRequest): ContributionClientResponse =
        try {
            commitClient.request(request)
        } catch (e: Exception) {
            logger.error("Failed to get commit contribution", e)
            throw e
        }
}
