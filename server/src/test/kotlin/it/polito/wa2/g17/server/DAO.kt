package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.products.Product
import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.messages.Message
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.tickets.*
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class DAO() {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

  fun getTicket(customer: Profile, product: Product): Ticket {
    return Ticket(
      customer = customer,
      product = product,
      problemType = ProblemType.HARDWARE
      //priorityLevel = Priority.LOW
    )
  }

  fun getMessage(/*user: Profile,*/ ticket: Ticket): Message {
    return Message(
      text = "This is a message",
      timestamp = Date(),
      //user = user,
      ticket = ticket
    )
  }

  fun getAttachment(/*message: Message*/): Attachment {
    return Attachment(
      name = "attachment1",
      type = "pdf",
      content = "content".toByteArray(),
      //message = message
    )
  }

  fun getStatusChange(/*ticket: Ticket, */status: Status, user: Profile): StatusChange {
    return StatusChange(
      status = status,
      user = user,
      timestamp = Date(),
      //ticket = ticket
    )
  }

  fun getAssignedTicketDTO(): AssignTicketDTO {
    return AssignTicketDTO(
      expertEmail = "expert@gmail.com",
      priority = Priority.LOW
    )
  }

  fun getPartialTicketDTO(ticket: Ticket): PartialTicketDTO {
    return ticket.toPartialDTO()
  }

  fun getCompleteTicketDTO(ticket: Ticket): CompleteTicketDTO {
    return ticket.toCompleteDTO()
  }

  fun getProfileCustomer(): Profile {
    var profile = Profile()
    return profile.apply {
      email = "customer@gmail.com"
      name = "Mario"
      surname = "Rossi"
    }
  }

  fun getProfileExpert(): Profile {
    var profile = Profile()
    return profile.apply {
      email = "expert@gmail.com"
      name = "Giacomo"
      surname = "Verdi"
    }
  }

  fun getProduct(): Product {
    var product = Product()
    return product.apply {
      ean = "4935531461206"
      name = "Samsung Galaxy S21"
      brand = "Samsung"
    }
  }

}