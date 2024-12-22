package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoSparkLineResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoResponse
import org.springframework.stereotype.Component

@Component
class GitRepoMapper {

    fun toGitRepoMemberResponses(gitRepoMembers: List<GitRepoMember>) = gitRepoMembers.map {
        toGitRepoMemberResponse(it)
    }

    private fun toGitRepoMemberResponse(it: GitRepoMember) = GitRepoMemberResponse(
        it.member.githubId,
        it.member.profileImage,
        it.gitRepoContribution?.commits,
        it.gitRepoContribution?.additions,
        it.gitRepoContribution?.deletions,
        it.member.isServiceMember(),
    )

    fun toGitRepoResponse(
        sparkLineResponse: GitRepoSparkLineResponse,
        gitRepoMembers: List<GitRepoMember>
    ): GitRepoResponse =
        GitRepoResponse(
            sparkLineResponse.all,
            toGitRepoMemberResponses(gitRepoMembers)
        )

    fun toGitRepoCompareResponse(first: GitRepoResponse, second: GitRepoResponse): GitRepoCompareResponse =
        GitRepoCompareResponse(first, second)
}
