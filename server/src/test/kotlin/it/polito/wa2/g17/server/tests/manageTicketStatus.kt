package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun closeTicketWhenOpen(
    ticketRepository: TicketRepository,
    profileRepository: ProfileRepository,
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    val customer = dao.getProfileClient()
    val product = dao.getProduct()

    profileRepository.save(customer)
    productRepository.save(product)

    val ticket = dao.getTicket(customer, product)

    val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)

    ticket.addStatus(statusChangeOpen)

    ticketRepository.save(ticket)

    val tickets = ticketRepository.findAll()

    val id = tickets[0].id

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val requestHeaders = HttpHeaders()
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(null, requestHeaders)

    val response = restTemplate.exchange(
        "http://localhost:$port/API/tickets/close/$id",
        HttpMethod.PUT,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

}

fun closeTicketWhenClosed(
    ticketRepository: TicketRepository,
    profileRepository: ProfileRepository,
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    val customer = dao.getProfileClient()
    val product = dao.getProduct()

    profileRepository.save(customer)
    productRepository.save(product)

    val ticket = dao.getTicket(customer, product)

    val statusChangeClosed = dao.getStatusChange(Status.CLOSED, customer)

    ticket.addStatus(statusChangeClosed)

    ticketRepository.save(ticket)

    val tickets = ticketRepository.findAll()

    val id = tickets[0].id

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val requestHeaders = HttpHeaders()
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(null, requestHeaders)

    val response = restTemplate.exchange(
        "http://localhost:$port/API/tickets/close/$id",
        HttpMethod.PUT,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

}

fun closeTicketWhenResolved(
    ticketRepository: TicketRepository,
    profileRepository: ProfileRepository,
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    val customer = dao.getProfileClient()
    val product = dao.getProduct()

    profileRepository.save(customer)
    productRepository.save(product)

    val ticket = dao.getTicket(customer, product)

    val statusChangeResolved = dao.getStatusChange(Status.RESOLVED, customer)

    ticket.addStatus(statusChangeResolved)

    ticketRepository.save(ticket)

    val tickets = ticketRepository.findAll()

    val id = tickets[0].id

    val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

    val requestHeaders = HttpHeaders()
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(null, requestHeaders)


    val response = restTemplate.exchange(
        "http://localhost:$port/API/tickets/close/$id",
        HttpMethod.PUT,
        requestEntity,
        Void::class.java
    )
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

}