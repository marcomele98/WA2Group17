package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO
import org.springframework.security.access.prepost.PostAuthorize

interface TicketService {

    fun createTicket(ticketDTO: CreateTicketDTO, email: String): CompleteTicketDTO

    fun getAllOpen(): List<PartialTicketDTO>

    fun getAllAssigned(): List<PartialTicketDTO>

    @PostAuthorize("returnObject.expertEmail == authentication.name || hasRole('MANAGER') || returnObject.customerEmail == authentication.name")
    fun getTicket(id: Long): CompleteTicketDTO


    fun getStatusHistory(id: Long): List<StatusChangeDTO>

    fun getUnresolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getResolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getAllByCustomerEmail(customerEmail: String): List<PartialTicketDTO>

    fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): CompleteTicketDTO

    fun closeTicket(ticketId: Long, userEmail: String, role: String): CompleteTicketDTO

    fun reopenTicket(ticketId: Long, email: String): CompleteTicketDTO

    fun resolveTicket(ticketId: Long, email: String): CompleteTicketDTO

    fun addMessage(ticketId: Long, message: MessageDTO, email: String, role: String): CompleteTicketDTO

}