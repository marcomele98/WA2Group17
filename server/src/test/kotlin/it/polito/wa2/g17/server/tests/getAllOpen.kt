package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.math.exp

fun getAllOpen(
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

  var ticket1 = dao.getTicket(customer, product)
  var ticket2 = dao.getTicket(customer, product).apply { problemType = ProblemType.SOFTWARE }

  val statusChangeOpen1 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress1 = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeOpen2 = dao.getStatusChange(Status.OPEN, customer)

  ticket1.addStatus(statusChangeOpen1)
  ticket1.addStatus(statusChangeInProgress1)
  ticket2.addStatus(statusChangeOpen2)

  ticket1 = ticketRepository.save(ticket1)
  ticket2 = ticketRepository.save(ticket2)

  val tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(1, tickets.size)

  val partialTicket2 = dao.getPartialTicketDTO(tickets[0])

  val partialTickets = listOf(partialTicket2)

  val token : AuthenticationResponseDTO = getToken("manager", "password", restTemplate, port)

  val requestHeaders = HttpHeaders()
  requestHeaders.setBearerAuth(token.accessToken)
  val requestEntity = HttpEntity(null, requestHeaders)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/open",
    HttpMethod.GET,
    requestEntity,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}


fun getAllOpenNoManager(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val expert = dao.getProfileExpert()
  val customer = dao.getProfileClient()
  val manager = dao.getProfileManager()
  val product = dao.getProduct()

  profileRepository.save(expert)
  profileRepository.save(customer)
  profileRepository.save(manager)
  productRepository.save(product)

  var ticket1 = dao.getTicket(customer, product)
  var ticket2 = dao.getTicket(customer, product).apply { problemType = ProblemType.SOFTWARE }

  val statusChangeOpen1 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress1 = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeOpen2 = dao.getStatusChange(Status.OPEN, customer)

  ticket1.addStatus(statusChangeOpen1)
  ticket1.addStatus(statusChangeInProgress1)
  ticket2.addStatus(statusChangeOpen2)


  ticket1 = ticketRepository.save(ticket1)
  ticket2 = ticketRepository.save(ticket2)

  val tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(1, tickets.size)


  val token : AuthenticationResponseDTO = getToken("expert", "password", restTemplate, port)

  val requestHeaders = HttpHeaders()
  requestHeaders.setBearerAuth(token.accessToken)
  val requestEntity = HttpEntity(null, requestHeaders)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/open",
    HttpMethod.GET,
    requestEntity,
    Void::class.java
    )

  Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)

}