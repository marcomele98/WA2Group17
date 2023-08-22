package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.profiles.ProfileDTO
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.messages.toDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.warranties.GetWarrantyDTO
import it.polito.wa2.g17.server.ticketing.warranties.toDTO
import java.sql.Timestamp

data class TicketWithMessagesDTO(
    val id: Long,
    val title: String,
    val problemType: ProblemType,
    val status: Status,
    val timestamp: String,
    val priority: Priority? = null,
    val warranty: GetWarrantyDTO,
    val messages: List<MessageDTO> = emptyList(),
    val expert: ProfileDTO? = null,
)

fun Ticket.toWithMessagesDTO(): TicketWithMessagesDTO {
    return TicketWithMessagesDTO(
        id!!,
        title,
        problemType,
        status,
        timestamp.toString(),
        priorityLevel,
        warranty.toDTO(),
        messages.map { it.toDTO() })
}