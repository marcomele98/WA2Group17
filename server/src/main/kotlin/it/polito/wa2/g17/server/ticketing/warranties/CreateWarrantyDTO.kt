package it.polito.wa2.g17.server.ticketing.warranties

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern

data class CreateWarrantyDTO(
    @field:Pattern(
        regexp = "[0-9]{13}",
        message = "Product EAN must be a sequence of 13 digits"
    )
    val productEan: String,
    @field:Min(1)
    val durationYears: Int,
    val typology: Typology,
    val customerEmail: String
)
