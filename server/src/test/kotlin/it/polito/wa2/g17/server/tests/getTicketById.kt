package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.CompleteTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun getTicketById(ticketRepository: TicketRepository, restTemplate: TestRestTemplate, port: Int) {

  val dao = DAO()

  var ticket = dao.getTicket()

  val statusChangeOpen = dao.getStatusChange(ticket, Status.OPEN)
  val statusChangeInProgress = dao.getStatusChange(ticket, Status.IN_PROGRESS)

  ticket.addStatus(statusChangeOpen)
  ticket.addStatus(statusChangeInProgress)
  ticket.apply { expertEmail = "expert@gmail.com" }

  val message = dao.getMessage(ticket)
  val attachment = dao.getAttachment(message)
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