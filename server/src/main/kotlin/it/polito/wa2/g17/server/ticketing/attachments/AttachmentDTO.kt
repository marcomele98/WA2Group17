package it.polito.wa2.g17.server.ticketing.attachments

import jakarta.validation.constraints.NotBlank

data class AttachmentDTO(
    val id: Long?,
    @NotBlank(message = "Name field is required")
    val name: String,
    //@NotBlank(message = "Type field is required")
    //val type: String,
)

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id!!, name)
}
