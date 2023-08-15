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
class AuthServiceImpl: AuthService {

    //TODO: interrogo keycloak da un repository com per profile

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/token")
    private lateinit var url: String


    var restTemplate: RestTemplate = RestTemplate()
    override fun login(authRequest: AuthenticationRequestDTO): AuthenticationResponseDTO {

        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("grant_type", "password")
        formData.add("client_id", "wa2g17-keycloak-client")
        formData.add("username", authRequest.email)
        formData.add("password", authRequest.password)
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)

        val requestEntity = HttpEntity(formData, headers)

        val responseEntity: ResponseEntity<AuthenticationResponseKeyClockDTO> = restTemplate.exchange(
            url, HttpMethod.POST, requestEntity,
            AuthenticationResponseKeyClockDTO::class.java
        )

        if (!responseEntity.statusCode.is2xxSuccessful) {
            println("Error: " + responseEntity.statusCode)
            throw Exception("Error: " + responseEntity.statusCode)
        }
        return AuthenticationResponseDTO(responseEntity.body!!.access_token, responseEntity.body!!.refresh_token)
    }

    override fun refresh(refreshToken: String): AuthenticationResponseDTO {

        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("grant_type", "refresh_token")
        formData.add("client_id", "wa2g17-keycloak-client")
        formData.add("refresh_token", refreshToken)
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)

        val requestEntity = HttpEntity(formData, headers)

        val responseEntity: ResponseEntity<AuthenticationResponseKeyClockDTO> = restTemplate.exchange(
            url, HttpMethod.POST, requestEntity,
            AuthenticationResponseKeyClockDTO::class.java
        )

        if (!responseEntity.statusCode.is2xxSuccessful) {
            println("Error: " + responseEntity.statusCode)
            throw Exception("Error: " + responseEntity.statusCode)
        }
        return AuthenticationResponseDTO(responseEntity.body!!.access_token, responseEntity.body!!.refresh_token)
    }


}