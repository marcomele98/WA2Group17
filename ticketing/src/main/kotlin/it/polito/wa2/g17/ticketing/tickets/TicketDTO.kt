package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.messages.*
import it.polito.wa2.g17.ticketing.status.Status
import it.polito.wa2.g17.ticketing.status.StatusChange
import it.polito.wa2.g17.ticketing.status.StatusChangeDTO
import it.polito.wa2.g17.ticketing.status.toDTO
import jakarta.validation.constraints.NotBlank
import java.util.*

data class TicketDTO(
    val id: Long?,
    val customerId: Long,
    val productEan: String,
    val status: Status? = null,
    @NotBlank(message = "Message field is required")
    val initialMessage: MessageDTO? = null,
    val statusHistory: List<StatusChangeDTO> = emptyList(),
    val messages: List<MessageDTO> = emptyList()
)

fun TicketDTO.withMessages(messages: List<MessageDTO>): TicketDTO {
    return this.copy(messages = messages)
}

fun TicketDTO.withStatusHistory(statusHistory: List<StatusChangeDTO>): TicketDTO {
    return this.copy(statusHistory = statusHistory)
}

fun Ticket.toDtoComplete(): TicketDTO {
    return TicketDTO(id!!, customerId, productEan, status, null, statusHistory.map { it.toDTO() }, messages.map { it.toDTO() })
}

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(id!!, customerId, productEan, status)
}



