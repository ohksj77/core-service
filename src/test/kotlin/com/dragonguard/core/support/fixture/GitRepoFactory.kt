package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.gitrepo.dto.GitOrgGitRepoResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoResponse

class GitRepoFactory {

    companion object {

        fun createGitRepoResponses() = listOf(
            "test-git-repo1",
            "test-git-repo2",
            "test-git-repo3",
        )

        fun createGitOrgGitRepoResponse() = GitOrgGitRepoResponse(
            "test-profile-image",
            setOf("test-git-repo1", "test-git-repo2", "test-git-repo3")
        )

        fun createGitRepoResponse() = GitRepoResponse(
            listOf(1, 2, 3, 4, 5),
            listOf(
                GitRepoMemberResponse(
                    "test-member1",
                    "test-member1-profile-image",
                    1000,
                    1000,
                    1000,
                    false,
                ),
                GitRepoMemberResponse(
                    "test-member2",
                    "test-member2-profile-image",
                    2000,
                    3000,
                    4000,
                    true,
                ),
            ),
        )

        fun createGitRepoCompareResponse() = GitRepoMemberCompareResponse(
            createGitRepoResponse(), createGitRepoResponse(),
        )
    }
}
