package com.dragonguard.core.domain.contribution

import com.dragonguard.core.support.auth.LoginTest
import com.dragonguard.core.support.fixture.ContributionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ContributionServiceTest : LoginTest() {
    @Autowired
    private lateinit var contributionService: ContributionService

    @Autowired
    private lateinit var contributionRepository: ContributionRepository

    @Test
    fun `회원의 기여도 조회`() {
        // given
        val contributions = ContributionFactory.createEntities(loginMember.id!!)
        contributionRepository.saveAll(contributions)

        // when
        val result = contributionService.getMemberContributions(loginMember.id!!)

        // then
        assertThat(result).isNotEmpty
        assertThat(result).hasSize(contributions.size)
    }
}
