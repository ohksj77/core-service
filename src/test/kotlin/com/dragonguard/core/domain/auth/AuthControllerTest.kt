package com.dragonguard.core.domain.auth

import com.dragonguard.core.config.security.jwt.JwtToken
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(AuthController::class)
class AuthControllerTest : RestDocsTest() {
    @MockBean
    private lateinit var authService: AuthService

    @Test
    fun `리프레시 토큰 발급`() {
        // given
        val jwtToken: JwtToken = JwtToken("1234.1234.1234", "4321.4321.4321")
        BDDMockito
            .given(authService.refresh(ArgumentMatchers.any<String>(), ArgumentMatchers.any<String>()))
            .willReturn(jwtToken)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("access_token", "4321.4321.4321")
                    .header("refresh_token", "apfawfawfa.awfsfawef2.r4svfv32"),
            )

        // then
        perform
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.access_token").value(jwtToken.accessToken))

        // docs
        perform
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "refresh jwt token",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.access_token").description("생성된 jwt 액세스 토큰"),
                        fieldWithPath("data.refresh_token").description("생성된 jwt 리프레시 토큰"),
                    )
                ),
            )
    }
}
