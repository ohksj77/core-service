package com.dragonguard.core.support.auth

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.auth.AuthorizedMember
import org.springframework.boot.test.context.TestComponent
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@TestComponent
class MockAuthorizedMemberArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.hasParameterAnnotation(AuthorizedMember::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any = Member("test-name", "test-githubId", "test-profileImage")
}
