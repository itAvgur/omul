package com.itavgur.omul.auth.web

import com.itavgur.omul.auth.service.RegistrationService
import com.itavgur.omul.auth.web.dto.JwtResponse
import com.itavgur.omul.auth.web.dto.SignUpRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth/register")
class RegistrationControllerV1(
    @Autowired val registrationService: RegistrationService
) {

    @Operation(summary = "Create credential for user", tags = ["signUser"])
    @PostMapping
    fun signUpV1(
        @Valid @RequestBody request: SignUpRequest,
    ): JwtResponse {

        return registrationService.register(request)
    }

    @GetMapping
    fun check(): ResponseEntity<Any> {

        return ResponseEntity(HttpStatus.ACCEPTED)
    }

}