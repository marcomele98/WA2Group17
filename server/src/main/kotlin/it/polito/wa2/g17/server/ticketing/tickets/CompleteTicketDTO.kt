package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.profiles.ProfileDTO
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import it.polito.wa2.g17.server.ticketing.messages.toDTO
import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.warranties.GetWarrantyWithCustomerDTO
import java.sql.Timestamp

data class CompleteTicketDTO(
    val id: Long,
    val title: String,
    val problemType: ProblemType,
    val status: Status,
    val timestamp: String,
    val priority: Priority? = null,
    val messages: List<MessageDTO> = emptyList(),
    val expert: ProfileDTO? = null,
    val warranty: GetWarrantyWithCustomerDTO? = null
)

fun CompleteTicketDTO.withWarranty(warranty: GetWarrantyWithCustomerDTO): CompleteTicketDTO {
    return this.copy(warranty = warranty)
}

fun Ticket.toCompleteDTO(): CompleteTicketDTO {
    return CompleteTicketDTO(id!!, title, problemType, status, timestamp.toString(), priorityLevel, messages.map { it.toDTO() })
}

fun CompleteTicketDTO.withExpert(expert: ProfileDTO): CompleteTicketDTO {
    return this.copy(expert = expert)
}
