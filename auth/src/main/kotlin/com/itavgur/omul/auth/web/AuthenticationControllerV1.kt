package com.itavgur.omul.auth.web

import com.itavgur.omul.auth.service.AuthenticationService
import com.itavgur.omul.auth.web.dto.AuthenticationRequest
import com.itavgur.omul.auth.web.dto.JwtResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthenticationControllerV1(
    @Autowired val authService: AuthenticationService
) {

    @Operation(summary = "Authenticate by login/pass, claim JWT", tags = ["authentication"])
    @PostMapping("/login/pass")
    fun authUserV1(
        @Valid @RequestBody request: AuthenticationRequest,
    ): JwtResponse {

        return authService.basicAuth(request)
    }

    @Operation(summary = "Validate authentication", tags = ["authentication"])
    @GetMapping("/validate")
    fun validateAuthenticationV1(): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.OK)
    }

}