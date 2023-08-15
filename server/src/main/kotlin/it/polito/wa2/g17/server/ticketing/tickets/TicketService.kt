package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import org.springframework.security.access.prepost.PostAuthorize

interface TicketService {

    fun createTicket(ticketDTO: CreateTicketDTO, email: String): TicketWithMessagesDTO

    fun getAllOpen(): List<PartialTicketDTO>

    fun getAllAssigned(): List<PartialTicketDTO>

    @PostAuthorize("returnObject.expert.email == authentication.name " +
            "|| hasRole('MANAGER') " +
            "|| returnObject.warranty.customer.email == authentication.name")
    fun getTicket(id: Long): CompleteTicketDTO


    fun getStatusHistory(id: Long): List<StatusChangeDTO>

    fun getUnresolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getResolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getAllByCustomerEmail(customerEmail: String): List<PartialTicketDTO>

    fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): TicketWithMessagesDTO

    fun closeTicket(ticketId: Long, userEmail: String, role: String): TicketWithMessagesDTO

    fun reopenTicket(ticketId: Long, email: String): TicketWithMessagesDTO

    fun resolveTicket(ticketId: Long, email: String): TicketWithMessagesDTO

    fun addMessage(ticketId: Long, message: MessageDTO, email: String, role: String): TicketWithMessagesDTO

}