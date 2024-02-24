package com.itavgur.omul.auth.web.dto

data class AuthenticationRequest(

    val login: String,
    val password: String
)