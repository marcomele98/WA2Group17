package it.polito.wa2.g17.server.tests

import it.polito.wa2.g17.server.DAO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.Priority
import it.polito.wa2.g17.server.ticketing.tickets.TicketRepository
import org.junit.jupiter.api.Assertions
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

fun getAllByCustomerEmail(ticketRepository: TicketRepository, restTemplate: TestRestTemplate, port: Int) {

  val dao = DAO()

  var ticket1 = dao.getTicket()
  ticketRepository.save(ticket1)

  var ticket2 = dao.getTicket().apply { productEan = "1234567890123"; priorityLevel = Priority.HIGH }
  ticketRepository.save(ticket2)

  val statusChangeOpen = dao.getStatusChange(ticket1, Status.OPEN)
  val statusChangeInProgress = dao.getStatusChange(ticket1, Status.IN_PROGRESS)
  val statusChangeOpen2 = dao.getStatusChange(ticket2, Status.OPEN)
  val statusChangeInProgress2 = dao.getStatusChange(ticket2, Status.IN_PROGRESS)
  val statusChangeClose2 = dao.getStatusChange(ticket2, Status.CLOSED)

  ticket1.addStatus(statusChangeOpen)
  ticket1.addStatus(statusChangeInProgress)
  ticket1.apply { expertEmail = "expert@gmail.com" }
  ticket2.addStatus(statusChangeOpen2)
  ticket2.addStatus(statusChangeInProgress2)
  ticket2.addStatus(statusChangeClose2)
  ticket2.apply { expertEmail = "expert@gmail.com" }

  var tickets = ticketRepository.findAll()
  Assertions.assertEquals(2, tickets.size)

  tickets = ticketRepository.findAllByCustomerEmail("customer@gmail.com")
  Assertions.assertEquals(2, tickets.size)

  var ticket3 = dao.getTicket()
    .apply { customerEmail = "costumer2@gmail.com"; productEan = "1234567833123"; priorityLevel = Priority.MEDIUM }

  ticketRepository.save(ticket3)

  tickets = ticketRepository.findAll()
  Assertions.assertEquals(3, tickets.size)

  tickets = ticketRepository.findAllByCustomerEmail("customer@gmail.com")
  Assertions.assertEquals(2, tickets.size)

  val partialTicket1 = dao.getPartialTicketDTO(tickets[0])
  var partialTicket2 = dao.getPartialTicketDTO(tickets[1])

  val partialTickets = listOf<PartialTicketDTO>(partialTicket1, partialTicket2)

  val response = restTemplate.exchange(
    "http://localhost:$port/API/customer/tickets?customerEmail=customer@gmail.com",
    HttpMethod.GET,
    null,
    object : ParameterizedTypeReference<List<PartialTicketDTO>>() {}
  )

  Assertions.assertEquals(HttpStatus.OK, response.statusCode)
  Assertions.assertEquals(partialTickets, response.body)

}