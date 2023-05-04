package it.polito.wa2.g17.server.ticketing.tickets

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
    return PartialTicketDTO(id!!, customer.email, product.ean, expert?.email, problemType, status, priorityLevel)
}



