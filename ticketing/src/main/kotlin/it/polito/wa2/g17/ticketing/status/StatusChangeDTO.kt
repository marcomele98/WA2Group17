package it.polito.wa2.g17.ticketing.status

import it.polito.wa2.g17.ticketing.tickets.Ticket
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.Date

data class StatusChangeDTO(
    val id: Long,
    val status: Status?,
    val timestamp: Date?,
    val userId: Long?,
)

fun StatusChange.toDTO(): StatusChangeDTO {
    return StatusChangeDTO(id!!, status, timestamp, userId)
}