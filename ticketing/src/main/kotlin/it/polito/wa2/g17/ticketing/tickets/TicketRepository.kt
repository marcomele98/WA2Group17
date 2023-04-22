package it.polito.wa2.g17.ticketing.tickets

import it.polito.wa2.g17.ticketing.status.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {
    fun findAllByStatusIn(status: List<Status>): List<Ticket>

}