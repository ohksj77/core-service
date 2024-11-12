package com.dragonguard.core.domain.search

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(SearchController::class)
class SearchControllerTest : RestDocsTest() {
    @Test
    fun `Github 회원 검색`() {
        // given
        BDDMockito
            .willDoNothing()
            .given(memberService)
            .updateContributions(any(Member::class.java))

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/search?type=MEMBER&q=dragon&page=1")
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
                    "search member",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    pathParameters(parameterWithName("type").description("검색 타입(MEMBER, GIT_REPO)")),
                    pathParameters(parameterWithName("q").description("검색어")),
                    pathParameters(parameterWithName("page").description("페이징에 사용될 페이지 (최소 1)")),
                ),
            )
    }

    @Test
    fun `Github Git Repo 검색`() {
        // given
        BDDMockito
            .willDoNothing()
            .given(memberService)
            .updateContributions(any(Member::class.java))

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .post("/search?type=GIT_REPO&q=dragon&page=1")
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
                    "search git repo",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    pathParameters(parameterWithName("type").description("검색 타입(MEMBER, GIT_REPO)")),
                    pathParameters(parameterWithName("q").description("검색어")),
                    pathParameters(parameterWithName("page").description("페이징에 사용될 페이지 (최소 1)")),
                ),
            )
    }
}
