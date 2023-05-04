package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.CompleteTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun getTicketById(
  ticketRepository: TicketRepository,
  profileRepository: ProfileRepository,
  productRepository: ProductRepository,
  restTemplate: TestRestTemplate,
  port: Int
) {

  val dao = DAO()

  val customer = dao.getProfileCustomer()
  val product = dao.getProduct()

  profileRepository.save(customer)
  productRepository.save(product)

  var ticket = dao.getTicket(customer, product)

  val statusChangeOpen = dao.getStatusChange(Status.OPEN, customer)
  val statusChangeInProgress = dao.getStatusChange(Status.IN_PROGRESS, customer)

  ticket.addStatus(statusChangeOpen)
  ticket.addStatus(statusChangeInProgress)

  val message = dao.getMessage(ticket)
  val attachment = dao.getAttachment()
  val attachments = listOf<Attachment>(attachment)

  message.addAttachments(attachments)

  ticket.addMessage(message)

  ticketRepository.save(ticket)

  val ticket1 = ticketRepository.findById(ticket.id!!).get()

  Assertions.assertEquals(ticket1, ticket)

  val messages = ticketRepository.findAllMessagesWithAttachments()
  Assertions.assertEquals(1, messages.size)

  val tickets = ticketRepository.findAllWithMessages()
  Assertions.assertEquals(1, tickets.size)

  val partialTicket = dao.getCompleteTicketDTO(tickets[0])

  val id = ticket1.id

  val response = restTemplate.exchange(
    "http://localhost:$port/API/$id",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<CompleteTicketDTO>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTicket, response.body)

}