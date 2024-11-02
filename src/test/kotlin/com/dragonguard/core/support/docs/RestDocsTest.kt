package com.dragonguard.core.support.docs

import com.dragonguard.core.config.security.jwt.JwtProvider
import com.dragonguard.core.config.security.jwt.JwtValidator
import com.dragonguard.core.domain.member.MemberService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@Import(RestDocsConfig::class)
@AutoConfigureRestDocs
@WebMvcTest
abstract class RestDocsTest {
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @MockBean
    private val jpaMetamodelMappingContext: JpaMetamodelMappingContext? = null

    @MockBean
    private val jwtValidator: JwtValidator? = null

    @MockBean
    private val jwtTokenProvider: JwtProvider? = null

    @MockBean
    private val memberService: MemberService? = null

    protected fun toRequestBody(obj: Any): String = objectMapper!!.writeValueAsString(obj)

    @BeforeEach
    fun setupMockMvc(
        ctx: WebApplicationContext,
        restDocumentationContextProvider: RestDocumentationContextProvider,
    ) {
        mockMvc =
            MockMvcBuilders
                .webAppContextSetup(ctx)
                .apply<DefaultMockMvcBuilder>(
                    MockMvcRestDocumentation.documentationConfiguration(
                        restDocumentationContextProvider,
                    ),
                ).addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
                .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
                .build()
    }
}
