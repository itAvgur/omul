package com.itavgur.omul.personnel.auth

import com.itavgur.omul.personnel.config.SecurityEnabled
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@ConditionalOnBean(SecurityEnabled::class)
class JwtTokenFilter(
    @Autowired private val jwtService: JwtService
) : OncePerRequestFilter(
) {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain

    ) {

        val authHeader = request.getHeader("Authorization")
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt: String = authHeader.substring(7)
        val userLogin: String = jwtService.extractUserName(jwt)
        if (StringUtils.isNotEmpty(userLogin) &&
            SecurityContextHolder.getContext().authentication == null
        ) {
            val grantedSubsystem = jwtService.extractSubsystem(jwt)
            val user = User(
                login = userLogin,
                linkedId = jwtService.extractLinkedId(jwt),
                subsystem = grantedSubsystem
            )
            if (jwtService.isTokenValid(jwt, user)) {
                val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                val authToken = UsernamePasswordAuthenticationToken(
                    user, null,
                    mutableListOf(SimpleGrantedAuthority(grantedSubsystem.name))
                )
                authToken.isAuthenticated
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                context.authentication = authToken
                SecurityContextHolder.setContext(context)
            }
        }

        filterChain.doFilter(request, response)
    }
}