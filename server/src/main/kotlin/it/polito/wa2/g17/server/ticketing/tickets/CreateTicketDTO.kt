package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class CreateTicketDTO(
    @field:Pattern(
        regexp = "[0-9]{13}",
        message = "Product EAN must be a sequence of 13 digits"
    )
    val productEan: String,

    val initialMessage: MessageDTO,

    val problemType: ProblemType
)
