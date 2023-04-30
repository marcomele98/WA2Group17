package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.tickets.Priority
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServerApplicationTests {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
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
            customerEmail = "customer@gmail.com",
            productEan = "4935531461206",
            priorityLevel = Priority.LOW,
            problemType = ProblemType.HARDWARE
        )

        val statusChangeOpen = StatusChange(
            Status.OPEN,
            userEmail = "customer@gmail.com",
            timestamp = Date(),
            ticket = ticket
        )

        val statusChangeInProgress = StatusChange(
            Status.IN_PROGRESS,
            userEmail = "manager@gmail.com",
            timestamp = Date(),
            ticket = ticket
        )

        ticket.addStatus(statusChangeOpen)
        ticket.addStatus(statusChangeInProgress)
        ticket.expertEmail = "expert@gmail.com"

        ticketRepository.save(ticket)

        var tickets = ticketRepository.findAll()

        assertEquals(1, tickets.size)

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
        assertEquals(1, tickets.size)
        ticket = tickets[0]

        assertEquals(100, results.size)
        assertEquals(1, results.values.count { it.statusCode == HttpStatus.OK })
        assertEquals(99, results.values.count { it.statusCode != HttpStatus.OK })
        assertEquals(Status.RESOLVED, ticket.status)

        assertEquals(
            listOf(Status.OPEN, Status.IN_PROGRESS, Status.RESOLVED),
            ticket.statusHistory
                .sortedBy { it.timestamp }
                .map { it.status }
        )

    }

}