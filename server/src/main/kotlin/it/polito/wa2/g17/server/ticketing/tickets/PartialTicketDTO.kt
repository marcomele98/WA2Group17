package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.ProblemType
import it.polito.wa2.g17.server.ticketing.status.Status

data class PartialTicketDTO(
    val id: Long,
    val customerEmail: String,
    val productEan: String,
    val expertEmail: String?,
    val problemType: ProblemType,
    val status: Status,
    val priority: Priority? = null,
)

fun Ticket.toPartialDTO(): PartialTicketDTO {
    return PartialTicketDTO(id!!, customerEmail, productEan, expertEmail, problemType, status, priorityLevel)
}



