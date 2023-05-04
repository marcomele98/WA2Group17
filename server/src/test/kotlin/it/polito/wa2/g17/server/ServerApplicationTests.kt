package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.tests.*
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

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

    @Autowired
    lateinit var dao: DAO

    @AfterEach
    fun setUp() {
        dao.clearDB()
    }

    @Test
    fun test1() {
        getTicketById(ticketRepository, restTemplate, port)
    }

    @Test
    fun test6() {
        ticketAssigned100Attempts(ticketRepository, restTemplate, port)
    }

    @Test
    fun test9() {
        ticketResolved100Attempts(ticketRepository, restTemplate, port)
    }

    @Test
    fun test11() {
        getAllByCustomerEmail(ticketRepository, restTemplate, port)
    }

    @Test
    fun test12() {
        ticketReopen100Attempts(ticketRepository, restTemplate, port)
    }

    @Test
    fun test13() {
        ticketClosed100Attempts(ticketRepository, restTemplate, port)
    }

}