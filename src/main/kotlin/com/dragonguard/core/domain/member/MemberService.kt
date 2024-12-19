package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.contribution.ContributionFacade
import com.dragonguard.core.domain.contribution.dto.ContributionRequest
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.domain.gitrepo.GitRepoService
import com.dragonguard.core.domain.member.dto.MemberDetailsResponse
import com.dragonguard.core.domain.member.dto.MemberProfileResponse
import com.dragonguard.core.domain.member.dto.MemberVerifyResponse
import com.dragonguard.core.domain.search.client.dto.SearchMemberClientResponse
import com.dragonguard.core.domain.search.dto.ServiceMembers
import com.dragonguard.core.global.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberMapper: MemberMapper,
    private val contributionFacade: ContributionFacade,
    private val gitRepoService: GitRepoService,
) {
    @Transactional
    fun joinIfNone(
        githubId: String,
        profileImage: String,
        userRequest: OAuth2UserRequest,
    ): Member {
        if (!memberRepository.existsByGithubId(githubId)) {
            memberRepository.save(
                memberMapper.toEntity(
                    githubId,
                    profileImage,
                ),
            )
        }

        val member: Member =
            getEntityByGithubId(githubId)

        if (member.hasNoAuthStep()) {
            member.join(profileImage)
        }
        member.updateGithubToken(userRequest.accessToken.tokenValue)
        return member
    }

    private fun getEntityByGithubId(githubId: String) =
        memberRepository.findByGithubId(githubId) ?: throw EntityNotFoundException.member()

    fun getEntity(id: Long): Member = memberRepository.findByIdOrNull(id) ?: throw EntityNotFoundException.member()

    fun updateContributions(member: Member) {
        contributionFacade.updateContributions(
            ContributionRequest(
                member.id!!,
                member.githubId,
                member.githubToken!!,
                member.organization?.id,
                member.organization?.organizationType,
            )
        )
    }

    fun getContributions(memberId: Long): List<ContributionResponse> =
        contributionFacade.getMemberContributions(memberId)

    fun getProfile(member: Member): MemberProfileResponse {
        val memberProfileRank = contributionFacade.getMemberProfileRank(member)
        return memberMapper.toProfileResponse(member, memberProfileRank)
    }

    fun isServiceMember(searchMemberResponses: List<SearchMemberClientResponse.Companion.SearchMemberClientResponseItem>): ServiceMembers =
        searchMemberResponses
            .filter {
                memberRepository.existsByGithubId(it.login)
            }.map { it.login }
            .let { ServiceMembers(it) }

    fun verify(member: Member): MemberVerifyResponse = MemberVerifyResponse(member.isLoginMember())

    fun delete(memberId: Long) = memberRepository.deleteById(memberId)

    fun getDetailsById(memberId: Long): MemberDetailsResponse {
        val member: Member =
            memberRepository.findByIdOrNull(memberId) ?: throw EntityNotFoundException.member()
        val gitRepos: List<String> = gitRepoService.getNamesByMemberId(memberId)
        val rank: Int = contributionFacade.getMemberRank(member)
        return memberMapper.toDetailsResponse(member, gitRepos, rank)
    }

    fun getDetailsByGithubId(githubId: String): MemberDetailsResponse {
        val member: Member =
            memberRepository.findByGithubId(githubId) ?: throw EntityNotFoundException.member()
        val gitRepos: List<String> = gitRepoService.getNamesByMemberId(member.id!!)
        val rank: Int = contributionFacade.getMemberRank(member)
        return memberMapper.toDetailsResponse(member, gitRepos, rank)
    }
}
