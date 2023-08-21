package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class CreateTicketDTO(

    @field:Pattern(regexp = "^[a-zA-Z0-9 ]{1,50}$")
    val title: String,

    @field:Min(1)
    val warrantyId: Long,

    val initialMessage: MessageDTO,

    val problemType: ProblemType
)
