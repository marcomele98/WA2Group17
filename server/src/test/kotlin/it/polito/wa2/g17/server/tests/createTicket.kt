package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
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

    var customer = dao.getProfileCustomer()
    var product = dao.getProduct()

    customer = profileRepository.save(customer)
    product = productRepository.save(product)

    var messageDTO : MessageDTO = dao.getMessageDTO(customer.email)

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO(product.ean, customer.email, messageDTO, ProblemType.HARDWARE)

    var completeTicketDTO = CompleteTicketDTO(
        1,
        customer.email,
        product.ean,
        null,
        ProblemType.HARDWARE,
        Status.OPEN,
        null,
        listOf(messageDTO)
    )

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
      "http://localhost:$port/API/customer/tickets",
      HttpMethod.POST,
      requestEntity,
      object : ParameterizedTypeReference<CompleteTicketDTO>() {}
    )
    Assertions.assertEquals(HttpStatus.CREATED, postResponse.statusCode)

    val id = postResponse.body!!.id
    Assertions.assertEquals(1,id)
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

    var customer = dao.getProfileCustomer()

    customer = profileRepository.save(customer)

    var messageDTO : MessageDTO = dao.getMessageDTO(customer.email)

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO("1234567890123", customer.email, messageDTO, ProblemType.HARDWARE)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
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

    var customer = dao.getProfileCustomer()

    customer = profileRepository.save(customer)

    var messageDTO : MessageDTO = dao.getMessageDTO(customer.email)

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO("12345678AA123", customer.email, messageDTO, ProblemType.HARDWARE)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
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

    var customer = dao.getProfileCustomer()

    customer = profileRepository.save(customer)

    var messageDTO : MessageDTO = dao.getMessageDTO(customer.email)

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO("12345678123456", customer.email, messageDTO, ProblemType.HARDWARE)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
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

    var customer = dao.getProfileCustomer()

    customer = profileRepository.save(customer)

    var messageDTO : MessageDTO = dao.getMessageDTO(customer.email)

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO("123456789012", customer.email, messageDTO, ProblemType.HARDWARE)


    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.statusCode)
}

fun createTicketCustomerEmptyString(
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var product = dao.getProduct()

    product = productRepository.save(product)

    var messageDTO : MessageDTO = dao.getMessageDTO("")

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO(product.ean, "", messageDTO, ProblemType.HARDWARE)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.statusCode)

}

fun createTicketWrongEmailFormat(
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    var product = dao.getProduct()

    product = productRepository.save(product)

    var messageDTO : MessageDTO = dao.getMessageDTO("aaabbbb.it")

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO(product.ean, "aaabbbb.it", messageDTO, ProblemType.HARDWARE)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
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

    var messageDTO : MessageDTO = dao.getMessageDTO("cus@test.com")

    var createTicketDTO : CreateTicketDTO = CreateTicketDTO(product.ean, "cus@test.com", messageDTO, ProblemType.HARDWARE)

    val objectMapper = ObjectMapper()
    val requestBody = objectMapper.writeValueAsString(createTicketDTO)
    val requestHeaders = HttpHeaders()
    requestHeaders.contentType = MediaType.APPLICATION_JSON
    val requestEntity = HttpEntity(requestBody, requestHeaders)

    var postResponse = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets",
        HttpMethod.POST,
        requestEntity,
        Void::class.java
    )

    Assertions.assertEquals(HttpStatus.NOT_FOUND, postResponse.statusCode)
}


