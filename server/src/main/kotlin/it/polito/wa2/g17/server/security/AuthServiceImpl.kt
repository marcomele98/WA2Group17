package it.polito.wa2.g17.server.security

import it.polito.wa2.g17.server.security.DTOs.AuthenticationRequestDTO
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.security.KeyClockDTOs.AuthenticationResponseKeyClockDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


@Service
class AuthServiceImpl(val authRepository: AuthRepository) : AuthService {

    //TODO: interrogo keycloak da un repository com per profile

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/token")
    private lateinit var url: String


    var restTemplate: RestTemplate = RestTemplate()
    override fun login(authRequest: AuthenticationRequestDTO): AuthenticationResponseDTO {

        val responseEntity: ResponseEntity<AuthenticationResponseKeyClockDTO> =
            authRepository.login(authRequest.email, authRequest.password)

        if (responseEntity.statusCode == HttpStatus.UNAUTHORIZED) {
            throw UnauthorizedException("Error: " + responseEntity.statusCode)
        }
        if(!responseEntity.statusCode.is2xxSuccessful) {
            println("Error: " + responseEntity.statusCode)
            throw Exception("Error: " + responseEntity.statusCode)
        }

        return AuthenticationResponseDTO(responseEntity.body!!.access_token, responseEntity.body!!.refresh_token)
    }

    override fun refresh(refreshToken: String): AuthenticationResponseDTO {

        val responseEntity: ResponseEntity<AuthenticationResponseKeyClockDTO> =
            authRepository.refresh(refreshToken)

        if(!responseEntity.statusCode.is2xxSuccessful) {
            println("Error: " + responseEntity.statusCode)
            throw Exception("Error: " + responseEntity.statusCode)
        }

        return AuthenticationResponseDTO(responseEntity.body!!.access_token, responseEntity.body!!.refresh_token)
    }


}