package it.polito.wa2.g17.server.ticketing.tickets

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AssignTicketDTO(
    @field:Email(message = "Email format not valid")
    @field:NotBlank(message = "Expert Email field is required")
    val expertEmail: String,
    val priority: Priority,
)