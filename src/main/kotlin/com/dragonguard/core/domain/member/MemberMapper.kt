package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.contribution.ContributionType
import com.dragonguard.core.domain.member.dto.MemberDetailsResponse
import com.dragonguard.core.domain.member.dto.MemberProfileResponse
import com.dragonguard.core.domain.rank.dto.ProfileRank
import org.springframework.stereotype.Component

@Component
class MemberMapper {
    fun toEntity(
        name: String,
        githubId: String,
        profileImage: String,
    ) = Member(
        name,
        githubId,
        profileImage,
    )

    fun toProfileResponse(
        member: Member,
        profileRank: ProfileRank,
    ): MemberProfileResponse =
        MemberProfileResponse(
            member.name,
            member.githubId,
            member.contributionNumOfType(ContributionType.COMMIT),
            member.contributionNumOfType(ContributionType.ISSUE),
            member.contributionNumOfType(ContributionType.PULL_REQUEST),
            member.contributionNumOfType(ContributionType.CODE_REVIEW),
            member.tier,
            member.authStep,
            member.profileImage,
            profileRank.rank,
            member.getTotalContribution(),
            member.organization?.name ?: EMPTY_ORGANIZATION_NAME,
            profileRank.organizationRank,
            profileRank.isLast,
            profileRank.memberGithubIds,
        )

    fun toDetailsResponse(member: Member, gitRepos: List<String>, rank: Int): MemberDetailsResponse =
        MemberDetailsResponse(
            member.contributionNumOfType(ContributionType.COMMIT),
            member.contributionNumOfType(ContributionType.ISSUE),
            member.contributionNumOfType(ContributionType.PULL_REQUEST),
            member.contributionNumOfType(ContributionType.CODE_REVIEW),
            member.profileImage,
            gitRepos,
            member.organization?.name ?: EMPTY_ORGANIZATION_NAME,
            rank,
        )

    companion object {
        private const val EMPTY_ORGANIZATION_NAME = ""
    }
}
