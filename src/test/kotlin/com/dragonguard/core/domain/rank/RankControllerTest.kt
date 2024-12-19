package com.dragonguard.core.domain.rank

import com.dragonguard.core.domain.organization.OrganizationType
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import com.dragonguard.core.support.fixture.RankFixture
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

@WebMvcTest(RankController::class)
class RankControllerTest : RestDocsTest() {

    @MockBean
    private lateinit var rankService: RankService

    @Test
    fun `회원 랭킹`() {
        // given
        val expected = RankFixture.createMemberRankResponse()

        BDDMockito
            .given(
                rankService.getMemberRank(any(Long::class.java), any(Long::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/ranks/members")
                    .queryParam("page", "0")
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
                    "get member ranks",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("page").description("페이징에 사용될 페이지 (최소 0)"),
                        parameterWithName("size").description("페이지당 데이터 수"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("회원 식별자"),
                        fieldWithPath("data[].github_id").description("깃허브 id"),
                        fieldWithPath("data[].tier").description("회원 티어"),
                        fieldWithPath("data[].profile_image").description("프로필 이미지 url"),
                        fieldWithPath("data[].contribution_amount").description("회원 기여도 총합"),
                    )
                ),
            )
    }

    @Test
    fun `조직 내 회원 랭킹`() {
        // given
        val expected = RankFixture.createMemberRankResponse()

        BDDMockito
            .given(
                rankService.getOrganizationMemberRank(
                    any(Long::class.java),
                    any(Long::class.java),
                    any(Long::class.java)
                ),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/ranks/organizations/{organizationId}/members", 1)
                    .queryParam("page", "0")
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
                    "get organization member ranks",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    pathParameters(
                        parameterWithName("organizationId").description("조직 식별자"),
                    ),
                    queryParameters(
                        parameterWithName("page").description("페이징에 사용될 페이지 (최소 0)"),
                        parameterWithName("size").description("페이지당 데이터 수"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("회원 식별자"),
                        fieldWithPath("data[].github_id").description("깃허브 id"),
                        fieldWithPath("data[].tier").description("회원 티어"),
                        fieldWithPath("data[].profile_image").description("프로필 이미지 url"),
                        fieldWithPath("data[].contribution_amount").description("회원 기여도 총합"),
                    )
                ),
            )
    }

    @Test
    fun `타입별 조직 랭킹`() {
        // given
        val expected = RankFixture.createOrganizationRankResponse()

        BDDMockito
            .given(
                rankService.getOrganizationRank(
                    any(OrganizationType::class.java),
                    any(Long::class.java),
                    any(Long::class.java)
                ),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/ranks/organizations")
                    .queryParam("type", "UNIVERSITY")
                    .queryParam("page", "0")
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
                    "get organization ranks by type",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("type").description("조직 타입"),
                        parameterWithName("page").description("페이징에 사용될 페이지 (최소 0)"),
                        parameterWithName("size").description("페이지당 데이터 수"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("회원 식별자"),
                        fieldWithPath("data[].name").description("깃허브 id"),
                        fieldWithPath("data[].contribution_amount").description("조직 기여도 총합"),
                        fieldWithPath("data[].type").description("조직 타입"),
                    )
                ),
            )
    }

    @Test
    fun `조직 전체 랭킹`() {
        // given
        val expected = RankFixture.createOrganizationRankResponse()

        BDDMockito
            .given(
                rankService.getAllOrganizationRank(
                    any(Long::class.java),
                    any(Long::class.java)
                ),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/ranks/organizations/all")
                    .queryParam("page", "0")
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
                    "get organization ranks all",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("page").description("페이징에 사용될 페이지 (최소 0)"),
                        parameterWithName("size").description("페이지당 데이터 수"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("회원 식별자"),
                        fieldWithPath("data[].name").description("깃허브 id"),
                        fieldWithPath("data[].contribution_amount").description("조직 기여도 총합"),
                        fieldWithPath("data[].type").description("조직 타입"),
                    )
                ),
            )
    }
}