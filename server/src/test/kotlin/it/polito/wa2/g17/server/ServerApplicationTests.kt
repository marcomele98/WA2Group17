package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.tests.*
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
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
            //registry.add("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation") {true}
            registry.add("spring.datasource.autoCommit") {false}
        }
    }

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var ticketRepository: TicketRepository

    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var dao: DAO

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test1() {
        getTicketById(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test2() {
        getTicketByIdWrong(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test3() {
        createMessage(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test4() {
        getAllOpen(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test5() {
        getAllAssigned(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test6() {
        getStatusHistory(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test7() {
        ticketAssigned100Attempts(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test8() {
        ticketResolved100Attempts(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test9() {
        createTicket(
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test10() {
        getAllByCustomerEmail(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test11() {
        ticketReopen100Attempts(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test12() {
        ticketClosed100Attempts(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }
    
    
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)

    fun test13() {
        createTicketProductDoesNotExist(
            profileRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test14() {
        createTicketWrongEanWithLetter(
            profileRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test15() {
        createTicketWrongEanLength14(
            profileRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test16() {
        createTicketWrongEanLength12(
            profileRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test17() {
        createTicketCustomerEmptyString(
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test18() {
        createTicketProfileNotFound(
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test19() {
        createTicketWrongEmailFormat(
            productRepository,
            restTemplate,
            port
        )
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test20() {
        closeTicketWhenOpen(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test21() {
        closeTicketWhenClosed(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun test22() {
        closeTicketWhenResolved(
            ticketRepository,
            profileRepository,
            productRepository,
            restTemplate,
            port
        )
    }

}