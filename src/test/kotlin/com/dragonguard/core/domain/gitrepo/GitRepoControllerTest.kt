package com.dragonguard.core.domain.gitrepo

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.support.docs.RestDocsTest
import com.dragonguard.core.support.docs.RestDocsUtils
import com.dragonguard.core.support.fixture.GitRepoFactory
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
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(GitRepoController::class)
class GitRepoControllerTest : RestDocsTest() {

    @MockBean
    private lateinit var gitRepoService: GitRepoService

    @Test
    fun `회원 본인 git-orgs 조회`() {
        // given
        val expected = GitRepoFactory.createGitRepoResponses()

        BDDMockito
            .given(
                gitRepoService.getNamesByMemberId(any(Long::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/git-repos/me")
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
                    "get git-repos",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data[]").description("git-org 이름"),
                    )
                ),
            )
    }

    @Test
    fun `git-orgs git-repos 조회`() {
        // given
        val expected = GitRepoFactory.createGitOrgGitRepoResponse()

        BDDMockito
            .given(
                gitRepoService.getGitOrgGitRepos(any(String::class.java), any(Member::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/git-repos/git-organizations")
                    .queryParam("name", "tukcom2023CD")
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
                    "get git-orgs git-repos",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("name").description("git-org 이름"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.profile_image").description("git-repo 프로필 이미지"),
                        fieldWithPath("data.git_repos[]").description("git-repo 이름"),
                    )
                ),
            )
    }

    @Test
    fun `git-repos 기여도 상세 조회`() {
        // given
        val expected = GitRepoFactory.createGitRepoResponse()

        BDDMockito
            .given(
                gitRepoService.requestAndGetGitRepoMembers(any(String::class.java), any(Long::class.java)),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/git-repos")
                    .queryParam("name", "tukcom2023CD/DragonGuard-JinJin")
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
                    "get git-repos details",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("name").description("git-repo 이름"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.spark_line[]").description("git-repo 기여도 추이"),
                        fieldWithPath("data.git_repo_members[].github_id").description("git-repo 멤버 githubId"),
                        fieldWithPath("data.git_repo_members[].profile_url").description("git-repo 멤버 프로필 이미지"),
                        fieldWithPath("data.git_repo_members[].commits").description("git-repo 멤버 커밋 수"),
                        fieldWithPath("data.git_repo_members[].additions").description("git-repo 멤버 코드 추가 수"),
                        fieldWithPath("data.git_repo_members[].deletions").description("git-repo 멤버 코드 삭제 수"),
                        fieldWithPath("data.git_repo_members[].is_service_member").description("git-repo 멤버 서비스 멤버 여부"),
                    )
                ),
            )
    }

    @Test
    fun `git-repos 기여도 상세 비교`() {
        // given
        val expected = GitRepoFactory.createGitRepoCompareResponse()

        BDDMockito
            .given(
                gitRepoService.compareGitReposMembers(
                    any(String::class.java),
                    any(String::class.java),
                    any(Long::class.java)
                ),
            ).willReturn(expected)

        // when
        val perform: ResultActions =
            mockMvc.perform(
                RestDocumentationRequestBuilders
                    .get("/git-repos/compare")
                    .queryParam("first", "tukcom2023CD/DragonGuard-JinJin")
                    .queryParam("second", "GitRank-v2/core-service")
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
                    "get git-repos compare",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer jwt 액세스 토큰"),
                    ),
                    queryParameters(
                        parameterWithName("first").description("첫번째 git-repo 이름"),
                        parameterWithName("second").description("두번째 git-repo 이름"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("http 상태 코드"),
                        fieldWithPath("message").description("성공 메시지"),
                        fieldWithPath("data.first.spark_line[]").description("git-repo 기여도 추이"),
                        fieldWithPath("data.first.git_repo_members[].github_id").description("git-repo 멤버 githubId"),
                        fieldWithPath("data.first.git_repo_members[].profile_url").description("git-repo 멤버 프로필 이미지"),
                        fieldWithPath("data.first.git_repo_members[].commits").description("git-repo 멤버 커밋 수"),
                        fieldWithPath("data.first.git_repo_members[].additions").description("git-repo 멤버 코드 추가 수"),
                        fieldWithPath("data.first.git_repo_members[].deletions").description("git-repo 멤버 코드 삭제 수"),
                        fieldWithPath("data.first.git_repo_members[].is_service_member").description("git-repo 멤버 서비스 멤버 여부"),
                        fieldWithPath("data.second.spark_line[]").description("git-repo 기여도 추이"),
                        fieldWithPath("data.second.git_repo_members[].github_id").description("git-repo 멤버 githubId"),
                        fieldWithPath("data.second.git_repo_members[].profile_url").description("git-repo 멤버 프로필 이미지"),
                        fieldWithPath("data.second.git_repo_members[].commits").description("git-repo 멤버 커밋 수"),
                        fieldWithPath("data.second.git_repo_members[].additions").description("git-repo 멤버 코드 추가 수"),
                        fieldWithPath("data.second.git_repo_members[].deletions").description("git-repo 멤버 코드 삭제 수"),
                        fieldWithPath("data.second.git_repo_members[].is_service_member").description("git-repo 멤버 서비스 멤버 여부"),
                    )
                ),
            )
    }
}
