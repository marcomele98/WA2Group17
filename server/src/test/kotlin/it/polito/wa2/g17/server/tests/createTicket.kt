package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.CompleteTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.CreateTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

fun createTicket(
    profileRepository: ProfileRepository,
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()
    var client = dao.getProfileClient()
    var product = dao.getProduct()

    client = profileRepository.save(client)
    product = productRepository.save(product)

    val messageDTO: MessageDTO = dao.getMessageDTO(client.email)

    val createTicketDTO: CreateTicketDTO = CreateTicketDTO(product.ean, messageDTO, ProblemType.HARDWARE)

    val completeTicketDTO = CompleteTicketDTO(
        1,
        client.email,
        product.ean,
        null,
        ProblemType.HARDWARE,
        Status.OPEN,
        null,
        listOf(messageDTO)
    )

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    val postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        object : ParameterizedTypeReference<CompleteTicketDTO>() {}
    )
    Assertions.assertEquals(HttpStatus.CREATED, postResponse.statusCode)

    val id = postResponse.body!!.id
    Assertions.assertEquals(1, id)
    Assertions.assertEquals(completeTicketDTO.id, postResponse.body!!.id)
    Assertions.assertEquals(completeTicketDTO.customerEmail, postResponse.body!!.customerEmail)
    Assertions.assertEquals(completeTicketDTO.productEan, postResponse.body!!.productEan)
    Assertions.assertEquals(completeTicketDTO.problemType, postResponse.body!!.problemType)
    Assertions.assertEquals(completeTicketDTO.status, postResponse.body!!.status)
    Assertions.assertEquals(completeTicketDTO.messages.size, postResponse.body!!.messages.size)
}


fun createTicketProductDoesNotExist(
    profileRepository: ProfileRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var customer = dao.getProfileClient()

    customer = profileRepository.save(customer)

    val messageDTO: MessageDTO = dao.getMessageDTO(customer.email)

    val createTicketDTO: CreateTicketDTO = CreateTicketDTO("1234567890123", messageDTO, ProblemType.HARDWARE)

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    val postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.NOT_FOUND, postResponse.statusCode)
}

fun createTicketWrongEanWithLetter(
    profileRepository: ProfileRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var customer = dao.getProfileClient()

    customer = profileRepository.save(customer)

    val messageDTO: MessageDTO = dao.getMessageDTO(customer.email)

    val createTicketDTO: CreateTicketDTO = CreateTicketDTO("12345678AA123", messageDTO, ProblemType.HARDWARE)

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    val postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.statusCode)
}


fun createTicketWrongEanLength14(
    profileRepository: ProfileRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var customer = dao.getProfileClient()

    customer = profileRepository.save(customer)

    val messageDTO: MessageDTO = dao.getMessageDTO(customer.email)

    val createTicketDTO: CreateTicketDTO = CreateTicketDTO("12345678123456", messageDTO, ProblemType.HARDWARE)

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    val postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.statusCode)
}

fun createTicketWrongEanLength12(
    profileRepository: ProfileRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var customer = dao.getProfileClient()

    customer = profileRepository.save(customer)

    val messageDTO: MessageDTO = dao.getMessageDTO(customer.email)

    val createTicketDTO: CreateTicketDTO = CreateTicketDTO("123456789012", messageDTO, ProblemType.HARDWARE)

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    val postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.statusCode)
}



fun createTicketProfileNotFound(
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var product = dao.getProduct()
    product = productRepository.save(product)

    val messageDTO: MessageDTO = dao.getMessageDTO("cus@test.com")

    val createTicketDTO: CreateTicketDTO = CreateTicketDTO(product.ean, messageDTO, ProblemType.HARDWARE)

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    val postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )

    Assertions.assertEquals(HttpStatus.NOT_FOUND, postResponse.statusCode)
}


