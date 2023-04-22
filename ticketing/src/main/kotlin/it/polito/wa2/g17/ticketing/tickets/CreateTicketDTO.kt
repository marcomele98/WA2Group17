package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.messages.MessageDTO
import jakarta.validation.constraints.NotNull

data class CreateTicketDTO(
    val productEan: String,
    val customerId: Long,
    @NotNull(message = "Initial Message field is required")
    val initialMessage: MessageDTO? = null
)
