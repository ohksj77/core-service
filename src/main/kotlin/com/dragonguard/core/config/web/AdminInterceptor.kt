package com.dragonguard.core.config.web

import com.dragonguard.core.config.security.jwt.JwtValidator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AdminInterceptor(
    private val jwtValidator: JwtValidator,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader(TOKEN_HEADER).substring(TOKEN_PREFIX_LENGTH)
        return jwtValidator.isAdmin(token)
    }

    companion object {
        private const val TOKEN_HEADER: String = "Authorization"
        private const val TOKEN_PREFIX: String = "Bearer "
        private const val TOKEN_PREFIX_LENGTH: Int = TOKEN_PREFIX.length
    }
}
