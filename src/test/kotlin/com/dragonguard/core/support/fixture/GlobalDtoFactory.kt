package com.dragonguard.core.support.fixture

import com.dragonguard.core.global.dto.IdResponse

class GlobalDtoFactory {

    companion object {
        fun createIdResponse(): IdResponse {
            return IdResponse(
                1L
            )
        }
    }
}
