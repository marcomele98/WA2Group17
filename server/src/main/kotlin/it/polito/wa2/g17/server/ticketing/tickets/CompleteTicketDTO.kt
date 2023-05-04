package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.messages.toDTO
import it.polito.wa2.g17.server.ticketing.status.Status

data class CompleteTicketDTO(
    val id: Long,
    val customerEmail: String,
    val productEan: String,
    val expertEmail: String?,
    val problemType: ProblemType,
    val status: Status,
    val priority: Priority? = null,
    val messages: List<MessageDTO> = emptyList()
)

fun Ticket.toCompleteDTO(): CompleteTicketDTO {
    return CompleteTicketDTO(
        id!!,
        customer.email,
        product.ean,
        expert?.email,
        problemType,
        status,
        priorityLevel,
        messages.map { it.toDTO() })
}