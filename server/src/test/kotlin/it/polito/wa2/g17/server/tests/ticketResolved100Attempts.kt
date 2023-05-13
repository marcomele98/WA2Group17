package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.security.DTOs.AuthenticationResponseDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun ticketResolved100Attempts(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val customer = dao.getProfileClient()
  val product = dao.getProduct()
  var expert = dao.getProfileExpert()
  val manager = dao.getProfileManager()

  expert.apply { skills = mutableListOf(ProblemType.HARDWARE) }

  profileRepository.save(customer)
  profileRepository.save(expert)
  profileRepository.save(manager)
  productRepository.save(product)

  var ticket = dao.getTicket(customer, product)

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, manager)

  ticket.addStatus(statusChangeOpen)
  ticket.addStatus(statusChangeInProgress)

  ticket.apply { this.expert = expert }

  ticketRepository.save(ticket)

  var tickets = ticketRepository.findAll()

  Assertions.assertEquals(1, tickets.size)

  val id = tickets[0].id

  //100 threads di cui max 10 in parallelo
  val executor = Executors.newFixedThreadPool(10)
  val results = ConcurrentHashMap<Int, ResponseEntity<Void>>()

  val token : AuthenticationResponseDTO = getToken("expert", "password", restTemplate, port)

  val requestHeaders = HttpHeaders()
  requestHeaders.contentType = MediaType.APPLICATION_JSON
  requestHeaders.setBearerAuth(token.accessToken)
  val requestEntity = HttpEntity(null, requestHeaders)

  for (i in 1..100) {
    executor.submit {
      val response = restTemplate.exchange(
        "http://localhost:$port/API/expert/tickets/resolve/$id",
        HttpMethod.PUT,
        requestEntity,
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
  Assertions.assertEquals(Status.RESOLVED, ticket.status)

  Assertions.assertEquals(
    listOf(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED),
    ticket.statusHistory
      .sortedBy { it.timestamp }
      .map { it.status }
  )

}