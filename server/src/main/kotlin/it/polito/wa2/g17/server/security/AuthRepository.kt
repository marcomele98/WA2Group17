package it.polito.wa2.g17.server.security

import it.polito.wa2.g17.server.security.KeyClockDTOs.AuthenticationResponseKeyClockDTO
import org.springframework.http.ResponseEntity

interface AuthRepository {
    fun login(email: String, password: String): ResponseEntity<AuthenticationResponseKeyClockDTO>
    fun refresh(refreshToken: String): ResponseEntity<AuthenticationResponseKeyClockDTO>
}