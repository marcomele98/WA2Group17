package it.polito.wa2.g17.server.ticketing.warranties

import it.polito.wa2.g17.server.products.ProductDTO
import it.polito.wa2.g17.server.products.toDTO
import it.polito.wa2.g17.server.profiles.ProfileDTO
import it.polito.wa2.g17.server.profiles.toDTO
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketDTO
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketWithoutWarrantyDTO
import it.polito.wa2.g17.server.ticketing.tickets.toPartialWithoutWarrantyDTO
import java.util.*


data class GetWarrantyDTO(
    val id: Long,
    val endDate: String,
    val product: ProductDTO,
    val valid: Boolean,
    val typology: Typology,
    val customer: ProfileDTO,
    val tickets: List<PartialTicketWithoutWarrantyDTO>
)
fun Warranty.toDTO(): GetWarrantyDTO {
    val valid = endDate > Date()
    return GetWarrantyDTO(
        id!!,
        endDate.toString(),
        product.toDTO(),
        valid,
        typology,
        customer.toDTO(),
        tickets.map { ticket -> ticket.toPartialWithoutWarrantyDTO() }
    )
}


