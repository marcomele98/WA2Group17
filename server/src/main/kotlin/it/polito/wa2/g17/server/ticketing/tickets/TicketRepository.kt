package it.polito.wa2.g17.server.ticketing.tickets

import it.polito.wa2.g17.server.ticketing.messages.Message
import it.polito.wa2.g17.server.ticketing.status.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {
    fun findAllByStatusIn(status: List<Status>): List<Ticket>

    fun findAllByCustomerEmail(customerEmail: String): List<Ticket>

    fun findAllByExpertEmailAndStatusIn(expertEmail: String, status: List<Status>): List<Ticket>

    //Only for testing purposes DO NOT USE
    @Query("SELECT t FROM Ticket t JOIN FETCH t.statusHistory")
    fun findAllEager(): List<Ticket>

    //Only for testing purposes DO NOT USE
    @Query("SELECT t FROM Ticket t JOIN FETCH t.messages")
    fun findAllWithMessages(): List<Ticket>

    //Only for testing purposes DO NOT USE
    @Query("SELECT m FROM Message m JOIN FETCH m.attachments")
    fun findAllMessagesWithAttachments(): List<Message>

}