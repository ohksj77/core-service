package com.dragonguard.core.support.auth

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Import(
    MockAuthorizedMemberArgumentResolver::class,
    MockAuthorizedMemberIdArgumentResolver::class,
)
@Configuration
class WebConfig(
    private val mockAuthorizedMemberArgumentResolver: MockAuthorizedMemberArgumentResolver,
    private val mockAuthorizedMemberIdArgumentResolver: MockAuthorizedMemberIdArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(mockAuthorizedMemberArgumentResolver)
        resolvers.add(mockAuthorizedMemberIdArgumentResolver)
    }
}
