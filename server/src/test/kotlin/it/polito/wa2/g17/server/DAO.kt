package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.tickets.*
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import java.util.*

@Component
class DAO() {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

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

  fun getAssignedTicketDTO(): AssignTicketDTO {
    return AssignTicketDTO(
      expertEmail = "exper@gmail.com",
      priority = Priority.LOW
    )
  }

  fun getPartialTicketDTO(ticket: Ticket): PartialTicketDTO {
    return ticket.toPartialDTO()
  }

  @Transactional
  fun clearDB() {
    entityManager.createNativeQuery("DELETE FROM status_changes").executeUpdate()
    entityManager.createNativeQuery("DELETE FROM message").executeUpdate()
    entityManager.createNativeQuery("DELETE FROM tickets").executeUpdate()
    entityManager.createNativeQuery("DELETE FROM attachment").executeUpdate()
    /*entityManager.createNativeQuery("DROP SEQUENCE IF EXISTS tickets_seq CASCADE").executeUpdate()
    entityManager.createNativeQuery("CREATE SEQUENCE tickets_seq INCREMENT BY 50 START 50 OWNED BY tickets.id").executeUpdate()*/
    //entityManager.createNativeQuery("ALTER TABLE tickets ALTER COLUMN id RESTART WITH 50").executeUpdate()
    //entityManager.createNativeQuery("SELECT setval('tickets_seq', 50, false);").executeUpdate()

    //entityManager.createNativeQuery("ALTER SEQUENCE tickets_seq RESTART WITH 50").executeUpdate()
  }
}