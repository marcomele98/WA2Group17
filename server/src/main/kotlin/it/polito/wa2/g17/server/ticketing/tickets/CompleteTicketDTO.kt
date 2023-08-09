package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.messages.toDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.warranties.GetWarrantyDTO
import it.polito.wa2.g17.server.ticketing.warranties.toDTO

data class CompleteTicketDTO(
    val id: Long,
    val expertEmail: String?,
    val problemType: ProblemType,
    val status: Status,
    val priority: Priority? = null,
    val warranty: GetWarrantyDTO,
    val messages: List<MessageDTO> = emptyList()
)

fun Ticket.toCompleteDTO(): CompleteTicketDTO {
    return CompleteTicketDTO(
        id!!,
        expert?.email,
        problemType,
        status,
        priorityLevel,
        warranty.toDTO(),
        messages.map { it.toDTO() })
}