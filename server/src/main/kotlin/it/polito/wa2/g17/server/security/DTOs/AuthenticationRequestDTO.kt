package it.polito.wa2.g17.server.security.DTOs

import jakarta.validation.constraints.NotBlank

data class AuthenticationRequestDTO(
    @field:NotBlank(message = "Username field is required")
    val username: String,

    @field:NotBlank(message = "Password field is required")
    val password: String
)