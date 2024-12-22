package com.dragonguard.core.global.exception

class RestClientException(
    override val message: String,
) : IllegalStateException(message) {
    companion object {
        fun commit() = RestClientException("Commit Client Error")

        fun issue() = RestClientException("Issue Client Error")

        fun pullRequest() = RestClientException("Pull Request Client Error")

        fun codeReview() = RestClientException("Code Review Client Error")

        fun memberGitRepo() = RestClientException("Member Git Repo Client Error")

        fun gitOrgGitRepo() = RestClientException("Git Org Git Repo Client Error")

        fun searchGitRepo() = RestClientException("Search Git Repo Client Error")

        fun searchMember() = RestClientException("Search Member Client Error")

        fun gitRepoDetails() = RestClientException("Git Repo Details Client Error")

        fun gitRepoLanguage() = RestClientException("Git Repo Language Client Error")

        fun gitRepoSparkLine() = RestClientException("Git Repo Spark Line Client Error")
        
        fun gitRepoInternal() = RestClientException("Git Repo Internal Client Error")
    }
}
