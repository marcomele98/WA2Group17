package it.polito.wa2.g17.server.ticketing.warranties

import it.polito.wa2.g17.server.products.ProductNotFoundException
import it.polito.wa2.g17.server.products.ProductRepository
import it.polito.wa2.g17.server.profiles.ProfileNotFoundException
import it.polito.wa2.g17.server.profiles.ProfileRepository
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
    override fun getWarrantyById(id: Long): GetWarrantyDTO {
        return warrantyRepository.findById(id).orElseThrow {
            WarrantyNotFoundException("Warranty with ID $id not found")
        }.toDTO()
    }

    override fun getWarrantiesByEmail(email: String): List<GetWarrantyDTO> {
        return warrantyRepository.findAllByCustomerEmail(email).map { it.toDTO() }
    }

    override fun createWarranty(warranty: CreateWarrantyDTO): GetWarrantyDTO {

        val product = productRepository.findById(warranty.productEan)
            .orElseThrow { ProductNotFoundException("Product with EAN ${warranty.productEan} not found") }

        val profile = profileRepository.findById(warranty.customerEmail)
            .orElseThrow { ProfileNotFoundException("Profile with email ${warranty.customerEmail} not found") }

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
            customer = profile
        )

        return warrantyRepository.save(newWarranty).toDTO()
    }
}