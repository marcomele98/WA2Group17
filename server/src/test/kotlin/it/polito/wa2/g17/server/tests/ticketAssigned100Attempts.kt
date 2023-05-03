package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun ticketAssigned100Attempts(ticketRepository: TicketRepository, restTemplate: TestRestTemplate, port: Int) {

  val dao = DAO()

  var ticket = dao.getTicket()
  val statusChangeOpen = dao.getStatusChange(ticket, Status.OPEN)

  val assignedTicketDTO = dao.getAssignedTicketDTO()

  ticket.addStatus(statusChangeOpen)

  ticketRepository.save(ticket)

  var tickets = ticketRepository.findAll()

  Assertions.assertEquals(1, tickets.size)

  //100 threads di cui max 10 in parallelo
  val executor = Executors.newFixedThreadPool(10)
  val results = ConcurrentHashMap<Int, ResponseEntity<Void>>()

  val objectMapper = ObjectMapper()

  for (i in 1..100) {
    executor.submit {
      val requestBody = objectMapper.writeValueAsString(assignedTicketDTO)
      val requestHeaders = HttpHeaders()
      requestHeaders.contentType = MediaType.APPLICATION_JSON
      val requestEntity = HttpEntity(requestBody, requestHeaders)
      val response = restTemplate.exchange(
        "http://localhost:$port/API/manager/tickets/assign/1",
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
  Assertions.assertEquals(Status.IN_PROGRESS, ticket.status)

  Assertions.assertEquals(
    listOf(Status.OPEN, Status.IN_PROGRESS),
    ticket.statusHistory
      .sortedBy { it.timestamp }
      .map { it.status }
  )

}