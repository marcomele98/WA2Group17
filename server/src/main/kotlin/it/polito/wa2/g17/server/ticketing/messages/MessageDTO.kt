package it.polito.wa2.g17.server.ticketing.messages

import it.polito.wa2.g17.server.ticketing.attachments.AttachmentDTO
import it.polito.wa2.g17.server.ticketing.attachments.toDTO
import jakarta.validation.constraints.NotBlank
import java.util.Date

data class MessageDTO(
    val id: Long?,
    @NotBlank(message = "User ID field is required")
    val userEmail: String?,
    @NotBlank(message = "Text field is required")
    val text: String,
    val timestamp: Date?,
    var attachmentIds: List<Long> = emptyList()
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(id!!, userEmail, text, timestamp)
}

fun MessageDTO.withAttachmentIds(attachments: List<Long>): MessageDTO {
    return this.copy(attachmentIds = attachments)
}

fun MessageDTO.withUserEmail(userEmail: String): MessageDTO {
    return this.copy(userEmail = userEmail)
}

fun MessageDTO.withTimestamp(date: Date): MessageDTO {
    return this.copy(timestamp = date)
}

fun MessageDTO.withUserId(userEmail: String): MessageDTO {
    return this.copy(userEmail = userEmail)
}
fun MessageDTO.toEntity(): Message {
    return Message(
        text,
        timestamp!!,
        userEmail!!
    )
}