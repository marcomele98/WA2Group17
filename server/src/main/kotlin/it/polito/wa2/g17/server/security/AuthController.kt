package it.polito.wa2.g17.server.security

import it.polito.wa2.g17.server.security.DTOs.AuthenticationRequestDTO
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/API")
class AuthController(private val authService: AuthService) {

    @PostMapping("login")
    fun login(@RequestBody authRequest: AuthenticationRequestDTO): AuthenticationResponseDTO {
        return authService.login(authRequest)
    }

}
