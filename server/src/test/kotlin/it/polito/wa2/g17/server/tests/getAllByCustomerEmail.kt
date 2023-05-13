package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.Priority
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun getAllByCustomerEmail(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val customer = dao.getProfileClient()
  val customer1 = dao.getProfileClient1()
  val manager = dao.getProfileManager()
  val product = dao.getProduct()

  profileRepository.save(customer)
  profileRepository.save(manager)
  profileRepository.save(customer1)
  productRepository.save(product)

  val ticket1 = dao.getTicket(customer, product)
  ticketRepository.save(ticket1)

  val ticket2 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.HIGH }
  ticketRepository.save(ticket2)

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeClosed = dao.getStatusChange(Status.CLOSED, customer)

  ticket1.addStatus(statusChangeOpen)
  ticket1.addStatus(statusChangeInProgress)
  ticket2.addStatus(statusChangeOpen)
  ticket2.addStatus(statusChangeInProgress)
  ticket2.addStatus(statusChangeClosed)

  var tickets = ticketRepository.findAll()
  Assertions.assertEquals(2, tickets.size)

  tickets = ticketRepository.findAllByCustomerEmail("client@mail.com")
  Assertions.assertEquals(2, tickets.size)

  profileRepository.save(customer1)

  val ticket3 = dao.getTicket(customer1, product)
    .apply {  priorityLevel = Priority.MEDIUM }

  ticketRepository.save(ticket3)

  tickets = ticketRepository.findAll()
  Assertions.assertEquals(3, tickets.size)

  tickets = ticketRepository.findAllByCustomerEmail("client@mail.com")
  Assertions.assertEquals(2, tickets.size)

  val partialTicket1 = dao.getPartialTicketDTO(tickets[0])
  val partialTicket2 = dao.getPartialTicketDTO(tickets[1])

  val partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2)

  val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

  val requestHeaders = HttpHeaders()
  requestHeaders.setBearerAuth(token.accessToken)
  val requestEntity = HttpEntity(null, requestHeaders)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/customer/tickets",
    HttpMethod.GET,
    requestEntity,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}


