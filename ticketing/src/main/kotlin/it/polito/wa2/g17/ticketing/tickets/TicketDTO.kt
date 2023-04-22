package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.messages.MessageDTO
import it.polito.wa2.g17.ticketing.status.Status
import it.polito.wa2.g17.ticketing.status.StatusChangeDTO

data class TicketDTO(
    val id: Long,
    val customerId: Long,
    val productEan: String,
    val status: Status,
    val statusHistory: List<StatusChangeDTO> = emptyList(),
    val messages: List<MessageDTO> = emptyList()
)

fun TicketDTO.withMessages(messages: List<MessageDTO>): TicketDTO {
    return this.copy(messages = messages)
}

fun TicketDTO.withStatusHistory(statusHistory: List<StatusChangeDTO>): TicketDTO {
    return this.copy(statusHistory = statusHistory)
}


fun Ticket.toDTO(status: Status): TicketDTO {
    return TicketDTO(id!!, customerId, productEan, status)
}




