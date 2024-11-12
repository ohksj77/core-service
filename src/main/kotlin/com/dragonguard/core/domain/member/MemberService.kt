package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.contribution.ContributionFacade
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.member.dto.MemberProfileResponse
import com.dragonguard.core.global.exception.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper,
    private val contributionFacade: ContributionFacade,
) {
    @Transactional
    fun joinIfNone(
        githubId: String,
        name: String,
        profileImage: String,
        userRequest: OAuth2UserRequest,
    ): Member {
        if (!memberRepository.existsByGithubId(githubId)) {
            memberRepository.save(
                memberMapper.toEntity(
                    name,
                    githubId,
                    profileImage,
                ),
            )
        }

        val member: Member =
            getEntityByGithubId(githubId)

        if (member.hasNoAuthStep()) {
            member.join(name, profileImage)
        }
        member.updateGithubToken(userRequest.accessToken.tokenValue)
        return member
    }

    private fun getEntityByGithubId(githubId: String) = memberRepository.findByGithubId(githubId) ?: throw EntityNotFoundException.member()

    fun getEntity(id: Long): Member = memberRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.member()

    fun updateContributions(member: Member) {
        contributionFacade.updateContributions(member)
    }

    fun getContributions(memberId: Long): List<ContributionResponse> = contributionFacade.getMemberContributions(memberId)

    fun getProfile(member: Member): MemberProfileResponse {
        val memberProfileRank = contributionFacade.getMemberProfileRank(member)
        return memberMapper.toProfileResponse(member, memberProfileRank)
    }
}
