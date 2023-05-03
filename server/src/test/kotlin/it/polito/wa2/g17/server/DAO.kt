package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.tickets.Priority
import it.polito.wa2.g17.server.ticketing.tickets.ProblemType
import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import java.util.*

class DAO {

  fun getTicket(): Ticket {
    return Ticket(
      customerEmail = "customer@gmail.com",
      productEan = "4935531461206",
      priorityLevel = Priority.LOW,
      problemType = ProblemType.HARDWARE
    )
  }

  fun getStatusChange(ticket: Ticket, status: Status): StatusChange {
    return StatusChange(
      status = status,
      userEmail = "customer@gmail.com",
      timestamp = Date(),
      ticket = ticket
    )
  }
}