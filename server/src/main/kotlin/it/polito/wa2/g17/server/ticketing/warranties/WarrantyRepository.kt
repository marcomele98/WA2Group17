package it.polito.wa2.g17.server.ticketing.warranties

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WarrantyRepository: JpaRepository<Warranty, Long> {
    fun findAllByCustomerEmail(customerEmail: String): List<Warranty>
}