package it.polito.wa2.g17.ticketing.attachments

import it.polito.wa2.g17.ticketing.messages.Message

data class AttachmentDTO(
    val id: Long?,
    val name: String,
    //val type: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id!!, name)
}

fun AttachmentDTO.toEntity(message: Message): Attachment {
    return Attachment(name, message)
}
