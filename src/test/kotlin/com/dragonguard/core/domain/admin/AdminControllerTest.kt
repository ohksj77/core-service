package com.dragonguard.core.domain.admin

import com.dragonguard.core.config.web.WebInterceptorConfig
import com.dragonguard.core.domain.admin.dto.AdminDecideRequest
import com.dragonguard.core.domain.organization.OrganizationStatus
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import com.dragonguard.core.support.fixture.OrganizationFactory
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
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
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(
    value = [AdminController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [WebInterceptorConfig::class]
        )
    ]
)
class AdminControllerTest : RestDocsTest() {

    @MockBean
    private lateinit var adminService: AdminService

    @Test
    fun `관리자 권한 확인`() {
        // given
        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/admin/check")
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
                    "check admin",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                ),
            )
    }

    @Test
    fun `조직 상태 업데이트`() {
        // given
        val expected = OrganizationFactory.createAdminOrganizationResponses()

        BDDMockito
            .given(adminService.decide(any(Long::class.java), any(AdminDecideRequest::class.java)))
            .willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/admin/organizations/{id}/decide", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toRequestBody(AdminDecideRequest(OrganizationStatus.APPROVED)))
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
                    "post admin decide",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    pathParameters(
                        parameterWithName("id").description("조직의 고유 식별자")
                    ),
                    requestFields(
                        fieldWithPath("organization_status").description("조직 상태"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("조직 고유 식별자"),
                        fieldWithPath("data[].name").description("조직명"),
                        fieldWithPath("data[].type").description("조직 유형"),
                        fieldWithPath("data[].email_endpoint").description("조직 이메일 엔드포인트"),
                    )
                ),
            )
    }

    @Test
    fun `조직 상태별 조회`() {
        // given
        val expected = OrganizationFactory.createAdminOrganizationResponses()

        BDDMockito
            .given(
                adminService.findOrganizationsByStatus(
                    any(OrganizationStatus::class.java),
                    any(Pageable::class.java)
                )
            )
            .willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/admin/organizations")
                    .queryParam("status", OrganizationStatus.APPROVED.name)
                    .queryParam("page", "0")
                    .queryParam("size", "10")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toRequestBody(AdminDecideRequest(OrganizationStatus.APPROVED)))
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
                    "get admin organization",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("status").description("조직 상태"),
                        parameterWithName("page").description("페이지 번호"),
                        parameterWithName("size").description("페이지 사이즈"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].id").description("조직 고유 식별자"),
                        fieldWithPath("data[].name").description("조직명"),
                        fieldWithPath("data[].type").description("조직 유형"),
                        fieldWithPath("data[].email_endpoint").description("조직 이메일 엔드포인트"),
                    )
                ),
            )
    }
}
