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
        val authHeader = request.getHeader(TOKEN_HEADER)
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 토큰이 없거나 유효하지 않습니다.")
            return false
        }

        val token = authHeader.substring(TOKEN_PREFIX_LENGTH)
        if (!jwtValidator.isAdmin(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.")
            return false
        }
        return true
    }

    companion object {
        private const val TOKEN_HEADER: String = "Authorization"
        private const val TOKEN_PREFIX: String = "Bearer "
        private const val TOKEN_PREFIX_LENGTH: Int = TOKEN_PREFIX.length
    }
}
