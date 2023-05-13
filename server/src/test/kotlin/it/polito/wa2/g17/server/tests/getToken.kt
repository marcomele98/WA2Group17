package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.security.DTOs.AuthenticationRequestDTO
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference

fun getToken(
    username: String,
    password: String,
    restTemplate: TestRestTemplate,
    port: Int
) : AuthenticationResponseDTO {

    var request : AuthenticationRequestDTO = AuthenticationRequestDTO(username, password)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(request)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
        "http://localhost:$port/API/login",
        HttpMethod.POST,
        requestEntity,
        object : ParameterizedTypeReference<AuthenticationResponseDTO>() {}
    )

    return postResponse.body!!
}