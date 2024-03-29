package it.polito.wa2.g17.server.ticketing.messages

import jakarta.validation.constraints.NotBlank
import java.util.*

data class MessageDTO(
    val id: Long?,
    val userEmail: String? = null,
    @field:NotBlank(message = "Text field is required")
    val text: String,
    val timestamp: Date?,
    var attachmentIds: List<Long> = emptyList()
)

fun Message.toDTO(): MessageDTO {
    val list : List<Long> = attachments.map { attachment -> attachment.id!! }
    return MessageDTO(id!!, userEmail, text, timestamp, attachmentIds = list)
}

fun MessageDTO.withTimestamp(date: Date): MessageDTO {
    return this.copy(timestamp = date)
}

fun MessageDTO.toEntity(): Message {
    return Message(
        text,
        timestamp!!
    )
}