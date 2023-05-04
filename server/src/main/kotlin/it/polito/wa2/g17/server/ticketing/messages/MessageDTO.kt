package it.polito.wa2.g17.server.ticketing.messages

import it.polito.wa2.g17.server.profiles.Profile
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.Date

data class MessageDTO(
    val id: Long?,
    @field:Email(message = "Email format not valid")
    @field:NotBlank(message = "User ID field is required")
    val userEmail: String?,
    @field:NotBlank(message = "Text field is required")
    val text: String,
    val timestamp: Date?,
    var attachmentIds: List<Long> = emptyList()
)

fun Message.toDTO(): MessageDTO {
    val list : List<Long> = attachments.map { attachment -> attachment.id!! }
    return MessageDTO(id!!, user!!.email, text, timestamp, attachmentIds = list)
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