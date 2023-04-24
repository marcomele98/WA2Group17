package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO

interface TicketService {

    fun createTicket(ticketDTO: CreateTicketDTO): CompleteTicketDTO

    fun getAllOpen(): List<PartialTicketDTO>

    fun getAllAssigned(): List<PartialTicketDTO>

    fun getTicket(id: Long): CompleteTicketDTO

    fun getStatusHistory(id: Long): List<StatusChangeDTO>

    fun getUnresolvedByExpertId(id: Long): List<PartialTicketDTO>

    fun getResolvedByExpertId(id: Long): List<PartialTicketDTO>

    fun getAllByCustomerId(customerId: Long): List<PartialTicketDTO>


}