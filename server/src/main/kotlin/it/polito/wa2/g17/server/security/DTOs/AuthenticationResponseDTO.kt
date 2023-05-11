package it.polito.wa2.g17.server.security.DTOs

data class AuthenticationResponseDTO(
    val accessToken: String,
    val refreshToken: String
)