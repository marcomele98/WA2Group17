package it.polito.wa2.g17.server.ticketing.attachments

interface AttachmentService {

    fun addAttachment(attachmentDTO: AttachmentDTO): AttachmentDTO

    fun getAttachment(id: Long): AttachmentDTO

}