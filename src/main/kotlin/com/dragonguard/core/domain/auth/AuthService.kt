package com.dragonguard.core.domain.auth

import com.dragonguard.core.config.security.jwt.JwtProvider
import com.dragonguard.core.config.security.jwt.JwtToken
import com.dragonguard.core.config.security.jwt.JwtValidator
import com.dragonguard.core.config.security.oauth.user.UserPrinciple
import com.dragonguard.core.domain.member.MemberService
import com.dragonguard.core.domain.member.exception.JwtProcessingException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberService: MemberService,
    private val jwtProvider: JwtProvider,
    private val jwtValidator: JwtValidator,
) {
    fun refresh(
        oldRefreshToken: String?,
        oldAccessToken: String?,
    ): JwtToken {
        validateTokens(oldRefreshToken, oldAccessToken)
        val userPrinciple: UserPrinciple = getAuthenticationByToken(oldRefreshToken)

        return getMemberAndUpdateRefreshToken(userPrinciple)
    }

    private fun getMemberAndUpdateRefreshToken(userPrinciple: UserPrinciple): JwtToken {
        val jwtToken: JwtToken = jwtProvider.createToken(userPrinciple)

        val member = memberService.getEntity(userPrinciple.getId())

        member.refreshToken = jwtToken.refreshToken
        return jwtToken
    }

    private fun getAuthenticationByToken(oldRefreshToken: String?): UserPrinciple =
        oldRefreshToken?.let { jwtValidator.getAuthentication(it).principal } as UserPrinciple

    private fun validateTokens(
        oldRefreshToken: String?,
        oldAccessToken: String?,
    ) {
        validateTokenNotBlank(oldRefreshToken, oldAccessToken)
        validateRefreshTokenNotExpired(oldRefreshToken)
    }

    private fun validateRefreshTokenNotExpired(oldRefreshToken: String?) {
        if (!jwtProvider.validateToken(oldRefreshToken)) {
            throw JwtProcessingException.expiredToken()
        }
    }

    private fun validateTokenNotBlank(
        oldRefreshToken: String?,
        oldAccessToken: String?,
    ) {
        if (oldRefreshToken.isNullOrBlank() || oldAccessToken.isNullOrBlank()) {
            throw JwtProcessingException.blankToken()
        }
    }
}
