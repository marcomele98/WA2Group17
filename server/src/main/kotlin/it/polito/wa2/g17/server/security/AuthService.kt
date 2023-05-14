package it.polito.wa2.g17.server.security

import it.polito.wa2.g17.server.security.DTOs.AuthenticationRequestDTO
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO

interface AuthService {
    fun login(authRequest: AuthenticationRequestDTO): AuthenticationResponseDTO

    fun refresh(refreshToken: String): AuthenticationResponseDTO
}