package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class CreateTicketDTO(

    val warrantyId: Long,

    val initialMessage: MessageDTO,

    val problemType: ProblemType
)
