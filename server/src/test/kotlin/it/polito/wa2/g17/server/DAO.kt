package it.polito.wa2.g17.server

import it.polito.wa2.g17.server.products.Product
import it.polito.wa2.g17.server.profiles.Profile
import it.polito.wa2.g17.server.ticketing.attachments.Attachment
import it.polito.wa2.g17.server.ticketing.messages.Message
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChange
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.server.ticketing.status.toDTO
import it.polito.wa2.g17.server.ticketing.tickets.*
import it.polito.wa2.g17.server.ticketing.warranties.Warranty
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class DAO() {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

  fun getTicket(customer: Profile, product: Product, warranty: Warranty): Ticket {
    return Ticket(
      customerEmail = customer.email,
      product = product,
      problemType = ProblemType.HARDWARE,
      warranty = warranty
      //priorityLevel = Priority.LOW
    )
  }

  fun getMessage(user: Profile, ticket: Ticket): Message {
    return Message(
      text = "This is a message",
      userEmail = user.email,
      ticket = ticket
    )
  }

  fun getMessageDTO(email: String): MessageDTO {
    return MessageDTO(
      null,
      userEmail = email,
      text = "This is a message",
      null
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
      userEmail = user.email,
      timestamp = Date(),
      //ticket = ticket
    )
  }

  fun getStatusChangeDTO(statusChange: StatusChange): StatusChangeDTO {
    return statusChange.toDTO()
  }

  fun getAssignedTicketDTO(): AssignTicketDTO {
    return AssignTicketDTO(
      expertEmail = "expert@mail.com",
      priority = Priority.LOW
    )
  }

  fun getPartialTicketDTO(ticket: Ticket): PartialTicketDTO {
    return ticket.toPartialDTO()
  }

  fun getCompleteTicketDTO(ticket: Ticket): TicketWithMessagesDTO {
    return ticket.toWithMessagesDTO()
  }

  fun getProfileManager(): Profile {
    var profile = Profile()
    return profile.apply {
      email = "manager@mail.com"
      name = "Marco"
      surname = "Mele"
    }
  }

  fun getProfileClient(): Profile {
    var profile = Profile()
    return profile.apply {
      email = "client@mail.com"
      name = "Francesco"
      surname = "Russo"
    }
  }

  fun getProfileClient1(): Profile {
    var profile = Profile()
    return profile.apply {
      email = "client1@mail.com"
      name = "Leonardo"
      surname = "Volpini"
    }
  }

  fun getProfileExpert(): Profile {
    var profile = Profile()
    return profile.apply {
      email = "expert@mail.com"
      name = "Federico"
      surname = "Rinaudi"
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