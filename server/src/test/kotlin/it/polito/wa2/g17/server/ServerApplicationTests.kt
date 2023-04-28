package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.tickets.CompleteTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.CreateTicketDTO
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
import java.util.Date
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

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
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var ticketRepository: TicketRepository


    @Test
    fun TicketResolved100Attempts() {

        /*val completeTicketDTO = CompleteTicketDTO(
            id = 1,
            productEan = "4935531461206",
            customerId = 1,
            status = Status.IN_PROGRESS,
            messages = emptyList<MessageDTO>()
        )*/

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
            Status.OPEN,
            userId = 1,
            timestamp = Date(),
            ticket = ticket
        )

        ticket.addStatus(statusChangeOpen)
        ticket.addStatus(statusChangeInProgress)

        ticketRepository.save(ticket)

        var tickets = ticketRepository.findAll()
        assertEquals(tickets.size, 1)
        ticket = tickets[0]

        val threads = mutableListOf<Thread>()
        val results = ConcurrentHashMap<Int, ResponseEntity<CompleteTicketDTO>>()

        for (i in 1..100) {
            threads.add(
                thread {
                    results[i] = restTemplate.exchange(
                        "http://localhost:${port}/API/expert/ticket/resolve/${ticket.id}",
                        HttpMethod.PUT,
                        null,
                        CompleteTicketDTO::class.java
                    )
                }
            )
        }

        threads.forEach { it.join() }

        assertEquals(
            results
                .filter {
                    it.value.statusCode == HttpStatus.OK
                }
                .count(),
            1
        )


        assertEquals(
            results
                .filter {
                    it.value.statusCode == HttpStatus.BAD_REQUEST
                }
                .count(),
            99
        )

        tickets = ticketRepository.findAll()
        assertEquals(tickets.size, 1)
        ticket = tickets[0]
        assertEquals(ticket.status, Status.RESOLVED)
        assertEquals(ticket.statusHistory.size, 3)
        assertEquals(ticket.statusHistory[2].status, Status.RESOLVED)
    }
}