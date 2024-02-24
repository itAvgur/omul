package com.itavgur.omul.auth.web.filters

import com.itavgur.omul.auth.service.AuthenticationService
import com.itavgur.omul.auth.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter(
    @Autowired private val authenticationService: AuthenticationService,
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
            val userDetails: UserDetails = authenticationService.userDetailsService()
                .loadUserByUsername(userLogin)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                context.authentication = authToken
                SecurityContextHolder.setContext(context)
            }
        }

        filterChain.doFilter(request, response)
    }
}