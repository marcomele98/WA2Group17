package it.polito.wa2.g17.ticketing.tickets

interface TicketService {

    fun createTicket(ticketDTO: CreateTicketDTO): TicketDTO

    fun getOpen(): List<TicketDTO>


    fun getAssigned(): List<TicketDTO>


    fun getTicket(id: Long): TicketDTO


}