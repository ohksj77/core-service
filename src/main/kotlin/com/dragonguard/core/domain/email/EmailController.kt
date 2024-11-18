package com.dragonguard.core.domain.email

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("email")
class EmailController(
    private val emailService: EmailService,
) {
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("resend/{id}")
    fun resend(@PathVariable id: Long) {
        emailService.resend(id)
    }
}
