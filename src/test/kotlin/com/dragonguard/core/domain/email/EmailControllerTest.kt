package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailCheckRequest
import com.dragonguard.core.domain.email.dto.EmailCheckResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(EmailController::class)
class EmailControllerTest : RestDocsTest() {

    @MockBean
    private lateinit var emailService: EmailService

    @Test
    fun `이메일 재전송`() {
        // given
        BDDMockito.willDoNothing().given(emailService).resend(any(Long::class.java))

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/email/{id}/resend", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"),
            )

        // then
        perform
            .andExpect(MockMvcResultMatchers.status().isNoContent())

        // docs
        perform
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "resend email",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    pathParameters(
                        parameterWithName("id").description("재전송할 이메일 코드의 고유 식별자")
                    )
                ),
            )
    }

    @Test
    fun `이메일 코드 확인`() {
        // given
        val expected = EmailCheckResponse(true)
        BDDMockito.given(
            emailService.check(
                any(EmailCheckRequest::class.java),
                any(Long::class.java),
                any(Member::class.java)
            )
        )
            .willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/email/{id}/check", 1)
                    .queryParam("code", "12345")
                    .queryParam("organizationId", "2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"),
            )

        // then
        perform
            .andExpect(MockMvcResultMatchers.status().isOk())

        // docs
        perform
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "check email",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    pathParameters(
                        parameterWithName("id").description("이메일 코드의 고유 식별자")
                    ),
                    queryParameters(
                        parameterWithName("code").description("이메일 코드"),
                        parameterWithName("organizationId").description("조직의 고유 식별자"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.is_valid_code").description("코드 유효 여부"),
                    )
                ),
            )
    }

    @Test
    fun `이메일 코드 제거`() {
        // given
        BDDMockito.willDoNothing().given(emailService).deleteCode(any(Long::class.java))

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .delete("/email/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"),
            )

        // then
        perform
            .andExpect(MockMvcResultMatchers.status().isNoContent())

        // docs
        perform
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "delete email",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    pathParameters(
                        parameterWithName("id").description("이메일 코드의 고유 식별자")
                    ),
                ),
            )
    }
}