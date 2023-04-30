package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.status.Status

data class PartialTicketDTO(
    val id: Long,
    val customerId: Long,
    val productEan: String,
    val status: Status,
    val priority: Priority? = null,
)

fun Ticket.toPartialDTO(): PartialTicketDTO {
    return PartialTicketDTO(id!!, customerId, productEan, status)
}



