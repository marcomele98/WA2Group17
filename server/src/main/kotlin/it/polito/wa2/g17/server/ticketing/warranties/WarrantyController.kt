package it.polito.wa2.g17.server.ticketing.warranties

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/API/customer/warranties")
class WarrantyCustomerController(private val warrantyService: WarrantyService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerWarranties(principal: Authentication): List<GetWarrantyDTO> {
        return warrantyService.getWarrantiesByEmail(principal.name)
    }

    @GetMapping("/{warrantyId}")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerWarrantyById(@PathVariable warrantyId: Long, principal: Authentication): GetWarrantyWithCustomerDTO {
        return warrantyService.getWarrantyById(warrantyId)
    }
}

@RestController
@Validated
@RequestMapping("/API/cashier/warranties")
class WarrantyCashierController(private val warrantyService: WarrantyService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createWarranty(@RequestBody warranty: CreateWarrantyDTO): GetWarrantyDTO {
        return warrantyService.createWarranty(warranty)
    }
}