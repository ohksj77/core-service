package com.dragonguard.core.global.auth

import com.dragonguard.core.config.security.oauth.user.UserPrinciple
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberService
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthorizedMemberArgumentResolver(
    private val memberService: MemberService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(AuthorizedMember::class.java) &&
            parameter.getParameterType() == Member::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val principal: UserPrinciple =
            SecurityContextHolder.getContext().authentication.principal as UserPrinciple
        return memberService.getEntity(principal.getMemberId())
    }
}
