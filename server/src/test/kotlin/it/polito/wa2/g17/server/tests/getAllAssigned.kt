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

fun getAllAssigned(
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

  val ticket1 = dao.getTicket(customer, product)

  val ticket2 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.HIGH }

  val ticket3 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.HARDWARE; priorityLevel = Priority.MEDIUM }

  val ticket4 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.LOW }

  val statusChangeOpen1 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress1 = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeResolved1 = dao.getStatusChange(Status.RESOLVED, customer)

  ticket1.addStatus(statusChangeOpen1)
  ticket1.addStatus(statusChangeInProgress1)
  ticket1.addStatus(statusChangeResolved1)
  ticketRepository.save(ticket1)

  val statusChangeOpen2 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress2 = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeClosed2 = dao.getStatusChange(Status.CLOSED, customer)

  ticket2.addStatus(statusChangeOpen2)
  ticket2.addStatus(statusChangeInProgress2)
  ticket2.addStatus(statusChangeClosed2)
  ticketRepository.save(ticket2)

  val statusChangeOpen3 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress3 = dao.getStatusChange(Status.IN_PROGRESS, manager)

  ticket3.addStatus(statusChangeOpen3)
  ticket3.addStatus(statusChangeInProgress3)
  ticketRepository.save(ticket3)

  val statusChangeOpen4 = dao.getStatusChange(Status.OPEN, customer)

  ticket4.addStatus(statusChangeOpen4)
  ticketRepository.save(ticket4)

  var tickets = ticketRepository.findAll()
  Assertions.assertEquals(4, tickets.size)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(1, tickets.size)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.IN_PROGRESS, Status.RESOLVED, Status.CLOSED))
  Assertions.assertEquals(3, tickets.size)

  val partialTicket1 = dao.getPartialTicketDTO(tickets[0])
  val partialTicket2 = dao.getPartialTicketDTO(tickets[1])
  val partialTicket3 = dao.getPartialTicketDTO(tickets[2])

  val partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2, partialTicket3)

  val token : AuthenticationResponseDTO = getToken("manager", "password", restTemplate, port)

  val requestHeaders = HttpHeaders()
  requestHeaders.setBearerAuth(token.accessToken)
  val requestEntity = HttpEntity(null, requestHeaders)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/assigned",
    HttpMethod.GET,
    requestEntity,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}


fun getAllAssignedNoManager(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val manager = dao.getProfileManager()
  val expert = dao.getProfileExpert()
  val customer = dao.getProfileClient()
  val product = dao.getProduct()

  profileRepository.save(manager)
  profileRepository.save(customer)
  profileRepository.save(expert)
  productRepository.save(product)

  val ticket1 = dao.getTicket(customer, product)

  val ticket2 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.HIGH }

  val ticket3 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.HARDWARE; priorityLevel = Priority.MEDIUM }

  val ticket4 = dao.getTicket(customer, product)
    .apply { problemType = ProblemType.SOFTWARE; priorityLevel = Priority.LOW }

  val statusChangeOpen1 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress1 = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeResolved1 = dao.getStatusChange(Status.RESOLVED, customer)

  ticket1.addStatus(statusChangeOpen1)
  ticket1.addStatus(statusChangeInProgress1)
  ticket1.addStatus(statusChangeResolved1)
  ticketRepository.save(ticket1)

  val statusChangeOpen2 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress2 = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeClosed2 = dao.getStatusChange(Status.CLOSED, customer)

  ticket2.addStatus(statusChangeOpen2)
  ticket2.addStatus(statusChangeInProgress2)
  ticket2.addStatus(statusChangeClosed2)
  ticketRepository.save(ticket2)

  val statusChangeOpen3 = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress3 = dao.getStatusChange(Status.IN_PROGRESS, manager)

  ticket3.addStatus(statusChangeOpen3)
  ticket3.addStatus(statusChangeInProgress3)
  ticketRepository.save(ticket3)

  val statusChangeOpen4 = dao.getStatusChange(Status.OPEN, customer)

  ticket4.addStatus(statusChangeOpen4)
  ticketRepository.save(ticket4)

  var tickets = ticketRepository.findAll()
  Assertions.assertEquals(4, tickets.size)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.OPEN))
  Assertions.assertEquals(1, tickets.size)

  tickets = ticketRepository.findAllByStatusIn(listOf(Status.IN_PROGRESS, Status.RESOLVED, Status.CLOSED))
  Assertions.assertEquals(3, tickets.size)

  val token : AuthenticationResponseDTO = getToken("expert", "password", restTemplate, port)

  val requestHeaders = HttpHeaders()
  requestHeaders.setBearerAuth(token.accessToken)
  val requestEntity = HttpEntity(null, requestHeaders)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/manager/tickets/assigned",
    HttpMethod.GET,
    requestEntity,
    Void::class.java
  )

  Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)

}