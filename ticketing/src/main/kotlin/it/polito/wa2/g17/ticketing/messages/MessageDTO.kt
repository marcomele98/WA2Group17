package it.polito.wa2.g17.ticketing.messages

import java.util.Date

data class MessageDTO(
    val id: Long,
    val title: String,
    val text: String,
    val timestamp: Date
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(id!!, title, text, timestamp)
}
