package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.messages.*
import it.polito.wa2.g17.ticketing.status.Status
import it.polito.wa2.g17.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.ticketing.status.toDTO

data class TicketDTO(
    val id: Long,
    val customerId: Long,
    val productEan: String,
    val status: Status,
    val statusHistory: List<StatusChangeDTO> = emptyList(),
    val messages: List<MessageDTO> = emptyList()
)

fun Ticket.toDtoComplete(): TicketDTO {
    return TicketDTO(id!!, customerId, productEan, status, statusHistory.map { it.toDTO() }, messages.map { it.toDTO() })
}

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(id!!, customerId, productEan, status)
}



