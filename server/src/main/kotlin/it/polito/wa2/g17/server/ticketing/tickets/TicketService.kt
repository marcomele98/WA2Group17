package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO

interface TicketService {

    fun createTicket(ticketDTO: CreateTicketDTO): CompleteTicketDTO

    fun getAllOpen(): List<PartialTicketDTO>

    fun getAllAssigned(): List<PartialTicketDTO>

    fun getTicket(id: Long): CompleteTicketDTO

    fun getStatusHistory(id: Long): List<StatusChangeDTO>

    fun getUnresolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getResolvedByExpertEmail(email: String): List<PartialTicketDTO>

    fun getAllByCustomerEmail(customerEmail: String): List<PartialTicketDTO>

    fun assignTicket(ticketId: Long, expertEmail: String, priority: Priority): CompleteTicketDTO

    fun closeTicket(ticketId: Long, userEmail: String): CompleteTicketDTO

    fun reopenTicket(ticketId: Long): CompleteTicketDTO

    fun resolveTicket(ticketId: Long): CompleteTicketDTO

    fun addMessage(ticketId: Long, message: MessageDTO): CompleteTicketDTO



}