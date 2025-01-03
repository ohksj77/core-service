package com.dragonguard.core.config.security.oauth

import com.dragonguard.core.config.security.oauth.user.UserPrincipleMapper
import com.dragonguard.core.domain.gitorg.GitOrgService
import com.dragonguard.core.domain.gitrepo.GitRepoService
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserServiceImpl(
    private val memberService: MemberService,
    private val gitOrgService: GitOrgService,
    private val gitRepoService: GitRepoService,
    private val userPrincipleMapper: UserPrincipleMapper,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val attributes = oAuth2User.attributes
        val githubId = attributes["login"] as String
        val profileImage = attributes["avatar_url"] as String

        val user: Member =
            memberService.joinIfNone(githubId, profileImage, userRequest)
        memberService.updateContributions(user)
        gitRepoService.updateGitRepo(user.id!!, user.githubToken!!, user.githubId)
        gitOrgService.updateGitOrg(user.id!!, user.githubToken!!, user.githubId)
        return userPrincipleMapper.mapToLoginUser(user)
    }
}
