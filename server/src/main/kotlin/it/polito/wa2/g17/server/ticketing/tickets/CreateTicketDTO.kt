package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.ProblemType
import it.polito.wa2.g17.server.ticketing.messages.MessageDTO
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

//TODO length è importato di hibernate e non di jakarta.validation, spacca?

data class CreateTicketDTO(
    @NotBlank(message = "Product EAN field is required")
    @Length(min = 13, max = 13, message = "Product EAN must be 13 characters long")
    @field:Pattern(
        regexp = "[0-9]+",
        message = "Product EAN must be a sequence of digits"
    )
    val productEan: String,
    @NotBlank(message = "Customer ID field is required")
    val customerId: Long,
    @NotBlank(message = "Initial Message field is required")
    val initialMessage: MessageDTO,
    @NotBlank(message = "Problem Type field is required")
    val problemType: ProblemType
)
