package com.dragonguard.core.domain.member

import com.dragonguard.core.domain.contribution.ContributionType
import com.dragonguard.core.domain.contribution.dto.ContributionResponse
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

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
                ),
            )
    }

    @Test
    fun `회원의 기여도 조회`() {
        // given
        val expected =
            listOf(
                ContributionResponse(ContributionType.ISSUE, 100, LocalDateTime.now()),
                ContributionResponse(ContributionType.COMMIT, 300, LocalDateTime.now()),
                ContributionResponse(ContributionType.CODE_REVIEW, 200, LocalDateTime.now()),
                ContributionResponse(ContributionType.PULL_REQUEST, 150, LocalDateTime.now()),
            )

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
                ),
            )
    }
}
