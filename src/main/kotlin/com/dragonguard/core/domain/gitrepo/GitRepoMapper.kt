package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.gitrepo.client.dto.GitRepoSparkLineResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberCompareResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoMemberResponse
import com.dragonguard.core.domain.gitrepo.dto.GitRepoResponse
import org.springframework.stereotype.Component

@Component
class GitRepoMapper {

    fun toGitRepoMemberResponses(gitRepoMembers: List<GitRepoMember>): List<GitRepoMemberResponse> =
        gitRepoMembers.map {
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

    fun toGitRepoCompareResponse(
        firstGitRepoMembers: List<GitRepoMember>,
        secondGitRepoMembers: List<GitRepoMember>
    ): GitRepoMemberCompareResponse =
        GitRepoMemberCompareResponse(
            toGitRepoMemberResponses(firstGitRepoMembers),
            toGitRepoMemberResponses(secondGitRepoMembers)
        )

    fun toGitRepoResponse(sparkLine: GitRepoSparkLineResponse, gitRepoMembers: List<GitRepoMember>): GitRepoResponse =
        GitRepoResponse(
            sparkLine.all,
            toGitRepoMemberResponses(gitRepoMembers),
        )
}
