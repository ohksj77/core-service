package com.dragonguard.core.support.auth

import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.MemberRepository
import com.dragonguard.core.support.fixture.MemberFactory
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LoginTest {
    @Autowired
    protected lateinit var memberRepository: MemberRepository

    protected lateinit var loginMember: Member

    @BeforeEach
    fun setup() {
        loginMember = memberRepository.save(MemberFactory.createEntity())
    }
}
