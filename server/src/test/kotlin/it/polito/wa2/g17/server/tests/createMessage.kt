package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.CompleteTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.CreateTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

fun createMessage(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  var customer = dao.getProfileCustomer()
  var product = dao.getProduct()

  customer = profileRepository.save(customer)
  product = productRepository.save(product)

  var ticket = dao.getTicket(customer, product)
  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  ticket.addStatus(statusChangeOpen)
  ticket = ticketRepository.save(ticket)

  val id = ticket.id

  var messageDTO : MessageDTO = dao.getMessageDTO(customer.email)

  val objectMapper = ObjectMapper()

  val addRequestBody = objectMapper.writeValueAsString(messageDTO)
  val addRequestHeaders = HttpHeaders()
  addRequestHeaders.contentType = MediaType.APPLICATION_JSON
  val addRequestEntity = HttpEntity(addRequestBody, addRequestHeaders)

  val putResponse = restTemplate.exchange(
    "http://localhost:$port/API/tickets/message/$id",
    HttpMethod.PUT,
    addRequestEntity,
    object : ParameterizedTypeReference<CompleteTicketDTO>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, putResponse.statusCode)

  Assertions.assertEquals(1, putResponse.body!!.messages.size)

  }



