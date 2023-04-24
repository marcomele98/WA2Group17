package it.polito.wa2.g17.server.ticketing.messages

import it.polito.wa2.g17.server.ticketing.attachments.AttachmentDTO
import it.polito.wa2.g17.server.ticketing.attachments.toDTO
import java.util.Date

data class MessageDTO(
    val id: Long?,
    val userId: Long?,
    val text: String,
    var attachments: List<AttachmentDTO> = emptyList(),
    val timestamp: Date?
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(id!!, userId, text, attachments.map { it.toDTO() }, timestamp)
}

fun MessageDTO.withTimestamp(date: Date): MessageDTO {
    return this.copy(timestamp = date)
}

fun MessageDTO.withUserId(userId: Long): MessageDTO {
    return this.copy(userId = userId)
}
fun MessageDTO.toEntity(): Message {
    return Message(
        text,
        timestamp!!,
        userId!!
    )
}