package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun getStatusHistory(
    ticketRepository: TicketRepository,
    profileRepository: ProfileRepository,
    productRepository: ProductRepository,
    restTemplate: TestRestTemplate,
    port: Int
) {

    val dao = DAO()

    val manager = dao.getProfileManager()
    val customer = dao.getProfileClient()
    val product = dao.getProduct()

    profileRepository.save(manager)
    profileRepository.save(customer)
    productRepository.save(product)

    var ticket = dao.getTicket(manager, product)

    val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
    val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, manager)
    val statusChangeClosed= dao.getStatusChange(Status.CLOSED, customer)

    ticket.addStatus(statusChangeOpen)
    ticket.addStatus(statusChangeInProgress)
    ticket.addStatus(statusChangeClosed)

    ticketRepository.save(ticket)

    val tickets = ticketRepository.findAllEager()

    ticket = tickets[0]

    val statusHistory = listOf<StatusChangeDTO>(
        dao.getStatusChangeDTO(statusChangeOpen),
        dao.getStatusChangeDTO(statusChangeInProgress),
        dao.getStatusChangeDTO(statusChangeClosed)
    )

    val id = ticket.id

    val token : AuthenticationResponseDTO = getToken("manager", "password", restTemplate, port)

    val requestHeaders = HttpHeaders()
    requestHeaders.setBearerAuth(token.accessToken)
    val requestEntity = HttpEntity(null, requestHeaders)

    val response = restTemplate.exchange(
        "http://localhost:$port/API/manager/tickets/statusHistory/$id",
        HttpMethod.GET,
        requestEntity,
        object : ParameterizedTypeReference<List<StatusChangeDTO>>() {}
    )

    Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    Assertions.assertEquals(statusHistory, response.body)

}