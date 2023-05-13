package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun ticketReopen100Attempts(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val customer = dao.getProfileClient()
  val manager = dao.getProfileManager()
  val product = dao.getProduct()

  profileRepository.save(customer)
  profileRepository.save(manager)
  productRepository.save(product)

  var ticket = dao.getTicket(customer, product)

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, manager)
  val statusChangeClosed = dao.getStatusChange(Status.CLOSED, customer)

  ticket.addStatus(statusChangeOpen)
  ticket.addStatus(statusChangeInProgress)
  ticket.addStatus(statusChangeClosed)

  ticketRepository.save(ticket)

  var tickets = ticketRepository.findAll()

  val id = tickets[0].id

  //100 threads di cui max 10 in parallelo
  val executor = Executors.newFixedThreadPool(10)
  val results = ConcurrentHashMap<Int, ResponseEntity<Void>>()

  val token : AuthenticationResponseDTO = getToken("client", "password", restTemplate, port)

  val addRequestHeaders = HttpHeaders()
  addRequestHeaders.contentType = MediaType.APPLICATION_JSON
  addRequestHeaders.setBearerAuth(token.accessToken)
  val addRequestEntity = HttpEntity(null, addRequestHeaders)

  for (i in 1..100) {
    executor.submit {
      val response = restTemplate.exchange(
        "http://localhost:$port/API/customer/tickets/reopen/$id",
        HttpMethod.PUT,
        addRequestEntity,
        Void::class.java
      )
      results[i] = response
    }
  }

  executor.shutdown()
  executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)

  tickets = ticketRepository.findAllEager()
  Assertions.assertEquals(1, tickets.size)
  ticket = tickets[0]

  Assertions.assertEquals(100, results.size)
  Assertions.assertEquals(1, results.values.count { it.statusCode == HttpStatus.OK })
  Assertions.assertEquals(99, results.values.count { it.statusCode != HttpStatus.OK })
  Assertions.assertEquals(Status.IN_PROGRESS, ticket.status)

  Assertions.assertEquals(
    listOf(Status.OPEN, Status.IN_PROGRESS, Status.CLOSED, Status.IN_PROGRESS),
    ticket.statusHistory
      .sortedBy { it.timestamp }
      .map { it.status }
  )

}