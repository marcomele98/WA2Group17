package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.core.Authentication

interface TicketService {

    fun createTicket(ticketDTO: CreateTicketDTO, email: String): CompleteTicketDTO

    fun getAllOpen(): List<PartialTicketDTO>

    fun getAllAssigned(): List<PartialTicketDTO>

    fun getTicket(id: Long): CompleteTicketDTO

    fun getStatusHistory(id: Long): List<StatusChangeDTO>

    fun getUnresolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getResolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getAllByCustomerEmail(customerEmail: String): List<PartialTicketDTO>

    fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): CompleteTicketDTO

    @PostAuthorize("returnObject.customerEmail == authentication.name || returnObject.expertEmail == authentication.name || hasRole('MANAGER')")
    fun closeTicket(ticketId: Long, userEmail: String): CompleteTicketDTO

    fun reopenTicket(ticketId: Long): CompleteTicketDTO

    fun resolveTicket(ticketId: Long): CompleteTicketDTO

    fun addMessage(ticketId: Long, message: MessageDTO): CompleteTicketDTO

}