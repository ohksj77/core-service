package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.gitorg.dto.GitOrgResponse

class GitOrgFactory {

    companion object {

        fun createGitorgResponses() = listOf(
            createGitOrgResponse(),
            createGitOrgResponse(),
            createGitOrgResponse(),
        )

        fun createGitOrgResponse() = GitOrgResponse(
            name = "test-name",
            profileImage = "test-profileImage",
        )
    }
}
