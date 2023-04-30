package it.polito.wa2.g17.server.ticketing.tickets

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AssignTicketDTO(
    @Email
    @NotBlank
    val expertEmail: String,
    @NotBlank
    val priority: Priority,
)