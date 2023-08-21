package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.status.Status
import it.polito.wa2.g17.server.ticketing.warranties.GetWarrantyDTO
import it.polito.wa2.g17.server.ticketing.warranties.toDTO

data class PartialTicketDTO(
    val id: Long,
    val title: String,
    val expertEmail: String?,
    val problemType: ProblemType,
    val status: Status,
    val warranty: GetWarrantyDTO,
    val priority: Priority? = null,
)

fun Ticket.toPartialDTO(): PartialTicketDTO {
    return PartialTicketDTO(
        id!!,
        title,
        expertEmail,
        problemType,
        status,
        warranty.toDTO(),
        priorityLevel
    )
}

data class PartialTicketWithoutWarrantyDTO(
    val id: Long,
    val title: String,
    val expertEmail: String?,
    val problemType: ProblemType,
    val status: Status,
    val priority: Priority? = null,
)


//orrendo, ma evitiamo un loop infinito
fun Ticket.toPartialWithoutWarrantyDTO(): PartialTicketWithoutWarrantyDTO {
    return PartialTicketWithoutWarrantyDTO(
        id!!,
        title,
        expertEmail,
        problemType,
        status,
        priorityLevel
    )
}





