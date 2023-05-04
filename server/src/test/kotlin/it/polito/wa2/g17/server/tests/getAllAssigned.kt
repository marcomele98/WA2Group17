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

fun getAllAssigned(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val customer = dao.getProfileCustomer()
  val product = dao.getProduct()

  profileRepository.save(customer)
  productRepository.save(product)

  var ticket1 = dao.getTicket(customer, product)

  var ticket2 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.HIGH }

  var ticket3 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.HARDWARE; priorityLevel = Priority.MEDIUM }

  var ticket4 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.LOW }

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, customer)
  val statusChangeResolved = dao.getStatusChange(Status.RESOLVED, customer)
  val statusChangeClosed = dao.getStatusChange(Status.CLOSED, customer)

  ticket1.addStatus(statusChangeOpen)
  ticket1.addStatus(statusChangeInProgress)
  ticket1.addStatus(statusChangeResolved)
  ticketRepository.save(ticket1)

  ticket2.addStatus(statusChangeOpen)
  ticket2.addStatus(statusChangeInProgress)
  ticket2.addStatus(statusChangeClosed)
  ticketRepository.save(ticket2)

  ticket3.addStatus(statusChangeOpen)
  ticket3.addStatus(statusChangeInProgress)
  ticketRepository.save(ticket3)

  ticket4.addStatus(statusChangeOpen)
  ticketRepository.save(ticket4)

  var tickets = ticketRepository.findAll()
  Assertions.assertEquals(4, tickets.size)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.IN_PROGRESS, Status.RESOLVED, Status.CLOSED))
  Assertions.assertEquals(3, tickets.size)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(1, tickets.size)

  val partialTicket1 = dao.getPartialTicketDTO(tickets[0])
  val partialTicket2 = dao.getPartialTicketDTO(tickets[1])
  val partialTicket3 = dao.getPartialTicketDTO(tickets[2])

  var partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2, partialTicket3)

  var response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/assigned",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}