package com.dragonguard.core.domain.member

import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import com.dragonguard.core.support.fixture.ContributionFactory
import com.dragonguard.core.support.fixture.MemberFactory
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(MemberController::class)
class MemberControllerTest : RestDocsTest() {
    @Test
    fun `회원의 기여도 업데이트`() {
        // given
        BDDMockito
            .willDoNothing()
            .given(memberService)
            .updateContributions(any(Member::class.java))

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/members/contributions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"),
            )

        // then
        perform
            .andExpect(MockMvcResultMatchers.status().isAccepted())

        // docs
        perform
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "update member contributions",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                ),
            )
    }

    @Test
    fun `회원의 기여도 조회`() {
        // given
        val expected =
            ContributionFactory.createResponse()

        BDDMockito
            .given(
                memberService
                    .getContributions(any(Long::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/members/contributions")
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
                    "get member contributions",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[].contribute_type").description("기여도 타입"),
                        fieldWithPath("data[].amount").description("기여도 개수"),
                        fieldWithPath("data[].created_at").description("기여도 db 생성 시점"),
                    )
                ),
            )
    }

    @Test
    fun `회원 메인 화면 프로필 정보 조회`() {
        // given
        val expected = MemberFactory.createProfileResponse()

        BDDMockito
            .given(
                memberService
                    .getProfile(any(Member::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/members/me")
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
                    "get member profile",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.github_id").description("깃허브 id"),
                        fieldWithPath("data.commits").description("커밋 개수"),
                        fieldWithPath("data.issues").description("이슈 개수"),
                        fieldWithPath("data.pull_requests").description("풀리퀘스트 개수"),
                        fieldWithPath("data.code_reviews").description("코드리뷰 개수"),
                        fieldWithPath("data.tier").description("깃랭크 티어"),
                        fieldWithPath("data.auth_step").description("인증 단계"),
                        fieldWithPath("data.profile_image").description("프로필 이미지"),
                        fieldWithPath("data.rank").description("전체 회원 랭크"),
                        fieldWithPath("data.contribution_amount").description("기여도 개수"),
                        fieldWithPath("data.organization").description("조직명"),
                        fieldWithPath("data.organization_rank").description("조직 내 랭크"),
                        fieldWithPath("data.is_last").description("조직 내 마지막 랭크 여부"),
                        fieldWithPath("data.member_github_ids").description("인접한 랭크 회원 깃허브 id"),
                    )
                ),
            )
    }

    @Test
    fun `회원 인증 여부 조회`() {
        // given
        val expected = MemberFactory.createVerifyResponse()

        BDDMockito
            .given(
                memberService
                    .verify(any(Member::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/members/verify")
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
                    "get verify member",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.is_login_member").description("조직 이메일 인증 완료 여부"),
                    )
                ),
            )
    }

    @Test
    fun `회원 탈퇴`() {
        // given
        BDDMockito.willDoNothing().given(memberService).delete(any(Long::class.java))

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .delete("/members")
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
                    "delete member",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                ),
            )
    }

    @Test
    fun `회원 개인 상세 조회`() {
        // given
        val expected = MemberFactory.createDetailsResponse()
        BDDMockito.given(memberService.getDetailsById(any(Long::class.java))).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/members/details/me")
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
                    "get member details me",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.commits").description("커밋 개수"),
                        fieldWithPath("data.issues").description("이슈 개수"),
                        fieldWithPath("data.pull_requests").description("풀리퀘스트 개수"),
                        fieldWithPath("data.code_reviews").description("코드리뷰 개수"),
                        fieldWithPath("data.profile_image").description("프로필 이미지"),
                        fieldWithPath("data.git_repos[]").description("깃허브 저장소명 목록"),
                        fieldWithPath("data.organization").description("조직명"),
                        fieldWithPath("data.rank").description("전체 회원 랭크"),
                    )
                ),
            )
    }

    @Test
    fun `타회원 상세 조회`() {
        // given
        val expected = MemberFactory.createDetailsResponse()
        BDDMockito.given(memberService.getDetailsByGithubId(any(String::class.java))).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/members/details")
                    .param("githubId", "ohksj77")
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
                    "get member details",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("githubId")
                            .description("깃허브 id")
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.commits").description("커밋 개수"),
                        fieldWithPath("data.issues").description("이슈 개수"),
                        fieldWithPath("data.pull_requests").description("풀리퀘스트 개수"),
                        fieldWithPath("data.code_reviews").description("코드리뷰 개수"),
                        fieldWithPath("data.profile_image").description("프로필 이미지"),
                        fieldWithPath("data.git_repos[]").description("깃허브 저장소명 목록"),
                        fieldWithPath("data.organization").description("조직명"),
                        fieldWithPath("data.rank").description("전체 회원 랭크"),
                    )
                ),
            )
    }
}
