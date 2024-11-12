package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.member.AuthStep
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.Tier
import com.dragonguard.core.domain.member.dto.MemberProfileResponse

class MemberFactory {
    companion object {
        fun createEntity(): Member = Member("test-name", "test-githubId", "test-profileImage")

        fun createProfileResponse(): MemberProfileResponse =
            MemberProfileResponse(
                "test-name",
                "test-githubId",
                100,
                200,
                300,
                400,
                Tier.GOLD,
                AuthStep.EMAIL,
                "test-profileImage",
                1,
                1000,
                "test-organization",
                3,
                false,
                listOf("test-1", "test-2", "test-3"),
            )
    }
}
