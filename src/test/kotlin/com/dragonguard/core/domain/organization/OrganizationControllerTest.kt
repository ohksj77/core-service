package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.dto.OrganizationJoinRequest
import com.dragonguard.core.domain.organization.dto.OrganizationRequest
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import com.dragonguard.core.support.fixture.GlobalDtoFactory
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(OrganizationController::class)
class OrganizationControllerTest : RestDocsTest() {

    @MockBean
    private lateinit var organizationService: OrganizationService

    @Test
    fun `조직 생성`() {
        // given
        val expected = GlobalDtoFactory.createIdResponse()

        BDDMockito
            .given(
                organizationService
                    .create(any(OrganizationRequest::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/organizations")
                    .content(
                        toRequestBody(
                            OrganizationRequest(
                                "test-org",
                                "tukorea.ac.kr",
                                OrganizationType.UNIVERSITY
                            )
                        )
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"),
            )

        // then
        perform
            .andExpect(MockMvcResultMatchers.status().isCreated())

        // docs
        perform
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "post organization",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestFields(
                        fieldWithPath("name").description("조직 이름"),
                        fieldWithPath("email_end_point").description("조직 도메인"),
                        fieldWithPath("organization_type").description("조직 유형")
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.id").description("생성된 조직의 고유 식별자")
                    )
                ),
            )
    }

    @Test
    fun `조직 회원 추가 요청`() {
        // given
        val expected = GlobalDtoFactory.createIdResponse()

        BDDMockito
            .given(
                organizationService
                    .join(any(Member::class.java), any(OrganizationJoinRequest::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/organizations/join")
                    .content(
                        toRequestBody(
                            OrganizationJoinRequest(
                                1L,
                                "ohksj77@tukorea.ac.kr",
                            )
                        )
                    )
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
                    "post organization join",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestFields(
                        fieldWithPath("organization_id").description("조직 고유 식별자"),
                        fieldWithPath("email").description("회원 이메일")
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.id").description("생성된 이메일 코드의 고유 식별자")
                    )
                ),
            )
    }
}