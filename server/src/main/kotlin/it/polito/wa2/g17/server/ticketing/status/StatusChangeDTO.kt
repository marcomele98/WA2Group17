package it.polito.wa2.g17.server.ticketing.status

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
