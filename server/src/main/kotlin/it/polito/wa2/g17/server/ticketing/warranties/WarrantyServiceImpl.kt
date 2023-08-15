package it.polito.wa2.g17.server.ticketing.warranties

import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.profiles.ProfileRepository
import it.polito.wa2.g17.server.profiles.toDTO
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@Service
@Transactional
class WarrantyServiceImpl(
    private val warrantyRepository: WarrantyRepository,
    private val productRepository: ProductRepository,
    private val profileRepository: ProfileRepository
) : WarrantyService {
    override fun getWarrantyById(id: Long): GetWarrantyWithCustomerDTO {

        val warranty = warrantyRepository.findById(id).orElseThrow {
            WarrantyNotFoundException("Warranty with ID $id not found")
        }
        val profile = profileRepository.findByEmail(warranty.customerEmail)
        return warranty.toGetWarrantyWithCustomerDTO().withCustomer(profile!!.toDTO())
    }

    override fun getWarrantiesByEmail(email: String): List<GetWarrantyDTO> {
        return warrantyRepository.findAllByCustomerEmail(email).map { it.toDTO() }
    }

    override fun createWarranty(warranty: CreateWarrantyDTO): GetWarrantyDTO {

        val product = productRepository.findById(warranty.productEan)
            .orElseThrow { ProductNotFoundException("Product with EAN ${warranty.productEan} not found") }

        val profile = profileRepository.findByEmail(warranty.customerEmail)
            ?: throw ProfileNotFoundException("Profile with email ${warranty.customerEmail} not found")

        val endDate = Date.from(
            LocalDate.now()
                .plusYears(warranty.durationYears.toLong())
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )

        val newWarranty = Warranty(
            endDate = endDate,
            startDate = Date(),
            product = product,
            typology = warranty.typology,
            customerEmail = profile.email
        )

        return warrantyRepository.save(newWarranty).toDTO()
    }
}