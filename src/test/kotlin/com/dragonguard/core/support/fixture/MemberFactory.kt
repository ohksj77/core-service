package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.member.Member

class MemberFactory {
    companion object {
        fun createEntity(): Member = Member("test-name", "test-githubId", "test-profileImage")
    }
}
