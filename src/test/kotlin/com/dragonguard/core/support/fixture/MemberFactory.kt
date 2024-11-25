package com.dragonguard.core.support.fixture

import com.dragonguard.core.domain.member.AuthStep
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.domain.member.Tier
import com.dragonguard.core.domain.member.dto.MemberDetailsResponse
import com.dragonguard.core.domain.member.dto.MemberProfileResponse
import com.dragonguard.core.domain.member.dto.MemberVerifyResponse

class MemberFactory {
    companion object {
        fun createEntity(): Member = Member("test-githubId", "test-profileImage")

        fun createProfileResponse(): MemberProfileResponse =
            MemberProfileResponse(
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

        fun createVerifyResponse(): MemberVerifyResponse = MemberVerifyResponse(true)
        fun createDetailsResponse(): MemberDetailsResponse = MemberDetailsResponse(
            100,
            200,
            300,
            400,
            "test-profileImage",
            listOf("test-repo-1", "test-repo-2"),
            "test-organization",
            1
        )
    }
}
