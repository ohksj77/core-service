package com.dragonguard.core.support.auth

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Import(
    MockAuthorizedMemberArgumentResolver::class,
    MockAuthorizedMemberIdArgumentResolver::class,
)
@TestConfiguration
class WebConfig(
    private val mockAuthorizedMemberArgumentResolver: MockAuthorizedMemberArgumentResolver,
    private val mockAuthorizedMemberIdArgumentResolver: MockAuthorizedMemberIdArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(0, mockAuthorizedMemberArgumentResolver)
        resolvers.add(1, mockAuthorizedMemberIdArgumentResolver)
    }
}
