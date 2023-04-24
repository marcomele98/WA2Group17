package it.polito.wa2.g17.server.ticketing.messages

import it.polito.wa2.g17.server.ticketing.tickets.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository



@Repository
interface MessageRepository : JpaRepository<Ticket, Long> {
}