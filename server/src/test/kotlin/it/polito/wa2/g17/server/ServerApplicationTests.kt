package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServerApplicationTests {

    companion object {
        @Container
        val postgres = PostgresContainer.instance
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            // Properties are loaded from application-test.properties
        }
    }

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var ticketRepository: TicketRepository

    @Test
    fun ticketResolved100Attempts() {

        var ticket = Ticket(
            customerId = 1,
            productEan = "4935531461206",
            priorityLevel = 2,
            expertId = 1
        )

        val statusChangeOpen = StatusChange(
            Status.OPEN,
            userId = 1,
            timestamp = Date(),
            ticket = ticket
        )

        val statusChangeInProgress = StatusChange(
            Status.IN_PROGRESS,
            userId = 1,
            timestamp = Date(),
            ticket = ticket
        )

        ticket.addStatus(statusChangeOpen)
        ticket.addStatus(statusChangeInProgress)

        ticketRepository.save(ticket)

        var tickets = ticketRepository.findAll()

        assertEquals(1, tickets.size)

        val executor = Executors.newFixedThreadPool(100)
        val results = ConcurrentHashMap<Int, ResponseEntity<Void>>()
        val successCounter = AtomicInteger(0)
        val badRequestCounter = AtomicInteger(0)

        for (i in 1..100) {
            executor.submit {
                val response = restTemplate.exchange(
                    "http://localhost:$port/API/expert/tickets/resolve/1",
                    HttpMethod.PUT,
                    null,
                    Void::class.java
                )
                results[i] = response
                if (response.statusCode.is2xxSuccessful) {
                    successCounter.incrementAndGet()
                } else {
                    badRequestCounter.incrementAndGet()
                }
            }
        }

        executor.shutdown()
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)

        tickets = ticketRepository.findAllEager()
        assertEquals(1, tickets.size)
        ticket = tickets[0]

        assertEquals(100, results.size)
        assertEquals(1, successCounter.get())
        assertEquals(99, badRequestCounter.get())
        assertTrue(results.values.any { it.statusCode.is2xxSuccessful })
        assertEquals(Status.RESOLVED, ticket.status)

        assertEquals(
            listOf(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED),
            ticket.statusHistory
                .sortedBy { it.timestamp }
                .map { it.status }
        )

    }

}