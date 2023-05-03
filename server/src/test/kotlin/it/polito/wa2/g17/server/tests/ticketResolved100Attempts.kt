package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun ticketResolved100Attempts(ticketRepository: TicketRepository, restTemplate: TestRestTemplate, port: Int) {

  var ticket = DAO().getTicket()
  val statusChangeOpen = DAO().getStatusChange(ticket, Status.OPEN)
  val statusChangeInProgress = DAO().getStatusChange(ticket, Status.IN_PROGRESS)

  ticket.addStatus(statusChangeOpen)
  ticket.addStatus(statusChangeInProgress)
  ticket.apply { expertEmail = "expert@gmail.com" }

  ticketRepository.save(ticket)

  var tickets = ticketRepository.findAll()

  Assertions.assertEquals(1, tickets.size)

  //100 threads di cui max 10 in parallelo
  val executor = Executors.newFixedThreadPool(10)
  val results = ConcurrentHashMap<Int, ResponseEntity<Void>>()

  for (i in 1..100) {
    executor.submit {
      val response = restTemplate.exchange(
        "http://localhost:$port/API/expert/tickets/resolve/1",
        HttpMethod.PUT,
        null,
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