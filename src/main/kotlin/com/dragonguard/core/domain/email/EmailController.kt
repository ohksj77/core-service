package com.dragonguard.core.domain.email

import com.dragonguard.core.domain.email.dto.EmailCheckRequest
import com.dragonguard.core.domain.email.dto.EmailCheckResponse
import com.dragonguard.core.domain.member.Member
import com.dragonguard.core.global.auth.AuthorizedMember
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ModelAttribute
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
    @PostMapping("{id}/resend")
    fun resend(@PathVariable id: Long) {
        emailService.resend(id)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("{id}/check")
    fun check(
        @Valid @ModelAttribute request: EmailCheckRequest,
        @PathVariable id: Long,
        @AuthorizedMember member: Member
    ): EmailCheckResponse =
        emailService.check(request, id, member)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) {
        emailService.deleteCode(id)
    }
}
