package it.polito.wa2.g17.server.ticketing.warranties

import it.polito.wa2.g17.server.products.ProductDTO
import it.polito.wa2.g17.server.products.toDTO
import it.polito.wa2.g17.server.profiles.ProfileDTO
import it.polito.wa2.g17.server.ticketing.tickets.PartialTicketWithoutWarrantyDTO
import it.polito.wa2.g17.server.ticketing.tickets.toPartialWithoutWarrantyDTO
import java.util.*


data class GetWarrantyWithCustomerDTO(
    val id: Long,
    val startDate: String,
    val endDate: String,
    val product: ProductDTO,
    val valid: Boolean,
    val typology: Typology,
    val tickets: List<PartialTicketWithoutWarrantyDTO>,
    val customer: ProfileDTO? = null
)

fun GetWarrantyWithCustomerDTO.withCustomer(profile: ProfileDTO): GetWarrantyWithCustomerDTO {
    return this.copy(customer = profile)
}

fun Warranty.toGetWarrantyWithCustomerDTO(): GetWarrantyWithCustomerDTO {
    val valid = endDate > Date()
    return GetWarrantyWithCustomerDTO(
        id!!,
        startDate.toString(),
        endDate.toString(),
        product.toDTO(),
        valid,
        typology,
        tickets.map { ticket -> ticket.toPartialWithoutWarrantyDTO() })
}
