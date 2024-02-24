package com.itavgur.omul.auth.service

import com.itavgur.omul.auth.dao.AuthenticationDao
import com.itavgur.omul.auth.exception.InvalidRequestException
import com.itavgur.omul.auth.web.dto.JwtResponse
import com.itavgur.omul.auth.web.dto.SignUpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegistrationService(
    @Autowired private val authenticationDao: AuthenticationDao,
    @Autowired private val jwtService: JwtService,
    @Autowired private val passwordEncoder: PasswordEncoder
) {

    fun register(request: SignUpRequest): JwtResponse {

        authenticationDao.getUserByLogin(request.login)?.let {
            throw InvalidRequestException("user with login ${request.login} is exist")
        }
        request.password

        val newUser = authenticationDao.createUser(request.toUser(passwordEncoder))
        return JwtResponse(jwtService.generateJwt(newUser))
    }

}