package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.Priority
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun getAllOpen(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  var customer = dao.getProfileCustomer()
  val product = dao.getProduct()

  profileRepository.save(customer)
  productRepository.save(product)

  val ticket1 = dao.getTicket(customer, product)

  val ticket2 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.HIGH }

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, customer)

  ticket1.addStatus(statusChangeOpen)
  ticket2.addStatus(statusChangeOpen)

  ticketRepository.save(ticket1)
  ticketRepository.save(ticket2)

  var tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(2, tickets.size)

  val partialTicket1 = dao.getPartialTicketDTO(tickets[0])
  val partialTicket2 = dao.getPartialTicketDTO(tickets[1])

  var partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2)

  var response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/open",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

  val ticket3 = dao.getTicket(customer, product)
    .apply { priorityLevel = Priority.MEDIUM }

  ticket3.addStatus(statusChangeOpen)

  ticketRepository.save(ticket3)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(3, tickets.size)

  val partialTicket3 = dao.getPartialTicketDTO(tickets[2])

  partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2, partialTicket3)

  response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/open",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

  ticket1.addStatus(statusChangeInProgress)

  ticketRepository.save(ticket1)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(2, tickets.size)

  partialTickets = listOf<PartialTicketDTO>(partialTicket2, partialTicket3)

  response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/open",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}