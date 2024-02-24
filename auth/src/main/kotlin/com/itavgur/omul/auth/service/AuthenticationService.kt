package com.itavgur.omul.auth.service

import com.itavgur.omul.auth.dao.AuthenticationDao
import com.itavgur.omul.auth.domain.User
import com.itavgur.omul.auth.web.dto.AuthenticationRequest
import com.itavgur.omul.auth.web.dto.JwtResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Autowired private val authenticationDao: AuthenticationDao,
    @Autowired private val jwtService: JwtService,
    @Lazy @Autowired private val authenticationManager: AuthenticationManager
) {

    fun basicAuth(request: AuthenticationRequest): JwtResponse {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.login, request.password)
        )

        val user = authentication.principal as User
        jwtService.evictTokenForUser(user)
        return JwtResponse(jwtService.generateJwt(user))
    }

    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { userLogin ->
            val userDetails = authenticationDao.getUserByLogin(userLogin)
                ?: throw UsernameNotFoundException("User not found")

            userDetails
        }
    }

}