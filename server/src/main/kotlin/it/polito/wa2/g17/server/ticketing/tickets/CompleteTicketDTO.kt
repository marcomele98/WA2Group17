package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.messages.toDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.status.toDTO

class CompleteTicketDTO(
    val id: Long,
    val customerId: Long,
    val productEan: String,
    val status: Status,
    val messages: List<MessageDTO> = emptyList()
)

fun Ticket.toCompleteDTO(): CompleteTicketDTO {
    return CompleteTicketDTO(
        id!!,
        customerId,
        productEan,
        status,
        messages.map { it.toDTO() })
}
