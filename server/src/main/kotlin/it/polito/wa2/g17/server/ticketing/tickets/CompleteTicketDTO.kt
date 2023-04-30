package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.ProblemType
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.messages.toDTO
import it.polito.wa2.g17.server.ticketing.status.Status

data class CompleteTicketDTO(
    val id: Long,
    val customerId: Long,
    val productEan: String,
    val status: Status,
    val priority: Priority? = null,
    val messages: List<MessageDTO> = emptyList()
)

fun Ticket.toCompleteDTO(): CompleteTicketDTO {
    return CompleteTicketDTO(
        id!!,
        customerId,
        productEan,
        status,
        priorityLevel,
        messages.map { it.toDTO() })
}
