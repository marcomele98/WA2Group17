package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.CompleteTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*

fun getTicketById(
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

  var createTicketDTO = dao.getCompleteTicketDTO(ticket)

  var getResponse = restTemplate.exchange(
    "http://localhost:$port/API/tickets/$id",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<CompleteTicketDTO>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, getResponse.statusCode)
  Assertions.assertEquals(getResponse.body, createTicketDTO)

}

fun getTicketByIdWrong(
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

  val id = ticket.id!! + 1

  var getResponse = restTemplate.exchange(
    "http://localhost:$port/API/tickets/$id",
    HttpMethod.GET,
    null,
    Void::class.java
  )

  Assertions.assertEquals(HttpStatus.NOT_FOUND, getResponse.statusCode)
}
