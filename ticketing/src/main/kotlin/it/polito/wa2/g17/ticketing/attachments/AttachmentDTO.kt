package it.polito.wa2.g17.ticketing.attachments

data class AttachmentDTO(
    val id: Long,
    val name: String,
    val type: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id!!, name, type)
}
