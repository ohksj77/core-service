package com.dragonguard.core.domain.organization

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.organization.dto.OrganizationJoinRequest
import com.dragonguard.core.domain.organization.dto.OrganizationRequest
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import com.dragonguard.core.support.fixture.GlobalDtoFactory
import com.dragonguard.core.support.fixture.OrganizationFactory
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
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
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
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
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
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

    @Test
    fun `조직 id 조회`() {
        // given
        val expected = GlobalDtoFactory.createIdResponse()

        BDDMockito
            .given(
                organizationService.getOrganizationId(any(String::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/organizations/id").queryParam("name", "test-org")
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
                    "get organization id",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("name")
                            .description("조직 이름")
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.id").description("조직의 고유 식별자")
                    )
                ),
            )
    }

    @Test
    fun `조직 타입으로 검색`() {
        // given
        val expected = OrganizationFactory.createOrganizationResponses()

        BDDMockito
            .given(
                organizationService.findByType(any(OrganizationType::class.java), any(Pageable::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/organizations").queryParam("type", OrganizationType.UNIVERSITY.name).queryParam("page", "0")
                    .queryParam("size", "20")
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
                    "get organization by type",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("type")
                            .description("조직 유형 (UNIVERSITY, COMPANY, HIGH_SCHOOL, ETC)"),
                        parameterWithName("page").description("페이지 번호"),
                        parameterWithName("size").description("페이지 크기")
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("조직의 고유 식별자"),
                        fieldWithPath("data[].name").description("조직 이름"),
                        fieldWithPath("data[].organization_type").description("조직 유형"),
                        fieldWithPath("data[].email_endpoint").description("조직 도메인"),
                        fieldWithPath("data[].contribution_amount").description("기여도 총합")
                    )
                ),
            )
    }

    @Test
    fun `조직 타입 및 검색어로 검색`() {
        // given
        val expected = OrganizationFactory.createOrganizationResponses()

        BDDMockito
            .given(
                organizationService.search(
                    any(OrganizationType::class.java),
                    any(String::class.java),
                    any(Pageable::class.java)
                ),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/organizations/search").queryParam("q", "한국공학")
                    .queryParam("type", OrganizationType.UNIVERSITY.name).queryParam("page", "0")
                    .queryParam("size", "20")
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
                    "get organization by type and name",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("q")
                            .description("검색어"),
                        parameterWithName("type")
                            .description("조직 유형"),
                        parameterWithName("page").description("페이지 번호"),
                        parameterWithName("size").description("페이지 크기")
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("조직의 고유 식별자"),
                        fieldWithPath("data[].name").description("조직 이름"),
                        fieldWithPath("data[].organization_type").description("조직 유형 (UNIVERSITY, COMPANY, HIGH_SCHOOL, ETC)"),
                        fieldWithPath("data[].email_endpoint").description("조직 도메인"),
                        fieldWithPath("data[].contribution_amount").description("기여도 총합")
                    )
                ),
            )
    }
}