package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailSendRequest

interface EmailSender {
    fun send(request: EmailSendRequest)
}