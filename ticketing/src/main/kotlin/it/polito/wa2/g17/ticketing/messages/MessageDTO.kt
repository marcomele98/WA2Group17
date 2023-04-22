package it.polito.wa2.g17.ticketing.messages

import it.polito.wa2.g17.ticketing.attachments.Attachment
import it.polito.wa2.g17.ticketing.attachments.AttachmentDTO
import it.polito.wa2.g17.ticketing.attachments.toDTO
import it.polito.wa2.g17.ticketing.attachments.toEntity
import it.polito.wa2.g17.ticketing.tickets.Ticket
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
fun MessageDTO.toEntity(ticket : Ticket): Message {
    val newMessage = Message(
        text,
        timestamp!!,
        ticket,
        userId!!
    )
    newMessage.attachments = attachments.map { it.toEntity(newMessage) }.toMutableList()

    return newMessage;
}