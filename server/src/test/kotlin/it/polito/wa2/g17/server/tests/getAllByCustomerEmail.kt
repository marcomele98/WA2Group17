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

fun getAllByCustomerEmail(
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

  var ticket1 = dao.getTicket(customer, product)
  ticketRepository.save(ticket1)

  var ticket2 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.HIGH }
  ticketRepository.save(ticket2)

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, customer)
  val statusChangeClosed = dao.getStatusChange(Status.CLOSED, customer)

  ticket1.addStatus(statusChangeOpen)
  ticket1.addStatus(statusChangeInProgress)
  ticket2.addStatus(statusChangeOpen)
  ticket2.addStatus(statusChangeInProgress)
  ticket2.addStatus(statusChangeClosed)

  var tickets = ticketRepository.findAll()
  Assertions.assertEquals(2, tickets.size)

  tickets = ticketRepository.findAllByCustomerEmail("customer@gmail.com")
  Assertions.assertEquals(2, tickets.size)

  var customer2 = dao.getProfileCustomer()
    .apply { email = "customer2@gmail.com" }

  profileRepository.save(customer2)

  var ticket3 = dao.getTicket(customer, product)
    .apply { this.customer = customer2; priorityLevel = Priority.MEDIUM }

  ticketRepository.save(ticket3)

  tickets = ticketRepository.findAll()
  Assertions.assertEquals(3, tickets.size)

  tickets = ticketRepository.findAllByCustomerEmail("customer@gmail.com")
  Assertions.assertEquals(2, tickets.size)

  val partialTicket1 = dao.getPartialTicketDTO(tickets[0])
  var partialTicket2 = dao.getPartialTicketDTO(tickets[1])

  val partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/customer/tickets?customerEmail=customer@gmail.com",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}