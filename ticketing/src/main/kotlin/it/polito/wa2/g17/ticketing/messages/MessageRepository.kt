package it.polito.wa2.g17.ticketing.messages

import it.polito.wa2.g17.ticketing.status.Status
import it.polito.wa2.g17.ticketing.tickets.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository



@Repository
interface MessageRepository : JpaRepository<Ticket, Long> {
}