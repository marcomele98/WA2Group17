package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.StatusChangeDTO

data class PartialTicketDTO(
    val id: Long,
    val customerId: Long,
    val productEan: String,
    val status: Status,
)

fun Ticket.toPartialDTO(): PartialTicketDTO {
    return PartialTicketDTO(id!!, customerId, productEan, status)
}



