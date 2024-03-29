package it.polito.wa2.g17.server.ticketing.warranties

interface WarrantyService {
    fun getWarrantyById(id: Long): GetWarrantyWithCustomerDTO
    fun getWarrantiesByEmail(email: String): List<GetWarrantyDTO>
    fun createWarranty(warranty: CreateWarrantyDTO): GetWarrantyDTO
}