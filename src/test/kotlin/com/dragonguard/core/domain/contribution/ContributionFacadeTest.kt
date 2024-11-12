package com.dragonguard.core.domain.contribution

import com.dragonguard.core.support.auth.LoginTest
import com.dragonguard.core.support.fixture.ContributionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ContributionFacadeTest : LoginTest() {
    @Autowired
    private lateinit var contributionFacade: ContributionFacade

    @Autowired
    private lateinit var contributionRepository: ContributionRepository

    @Test
    fun `회원의 기여도 조회`() {
        // given
        val contributions = ContributionFactory.createEntities(loginMember)
        contributionRepository.saveAll(contributions)

        // when
        val result = contributionFacade.getMemberContributions(loginMember.id!!)

        // then
        assertThat(result).isNotEmpty
        assertThat(result).hasSize(contributions.size)
    }
}
